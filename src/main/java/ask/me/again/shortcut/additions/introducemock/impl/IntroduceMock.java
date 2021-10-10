package ask.me.again.shortcut.additions.introducemock.impl;

import ask.me.again.shortcut.additions.introducemock.entities.ExececutionExtractor;
import ask.me.again.shortcut.additions.introducemock.exceptions.*;
import ask.me.again.shortcut.additions.introducemock.impl.extractors.ConstructorExtractorImpl;
import ask.me.again.shortcut.additions.introducemock.impl.extractors.MethodExtractorImpl;
import ask.me.again.shortcut.additions.introducemock.entities.ExecutionTarget;
import ask.me.again.shortcut.additions.introducemock.impl.targets.FieldTargetImpl;
import ask.me.again.shortcut.additions.introducemock.impl.targets.VariableTargetImpl;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.*;
import java.util.stream.Collectors;

public class IntroduceMock {

  private final Project project;
  private final Editor editor;
  private final PsiElementFactory factory;
  private final PsiFile psiFile;

  private final Map<ExececutionExtractor, IntroduceExtractors> selection = new HashMap<>();
  private final Map<ExecutionTarget, IntroduceTarget> targetMap = new HashMap<>();

  public IntroduceMock(AnActionEvent e) {
    project = e.getProject();
    editor = e.getData(CommonDataKeys.EDITOR);
    psiFile = e.getData(CommonDataKeys.PSI_FILE);

    factory = JavaPsiFacade.getElementFactory(project);

    selection.put(ExececutionExtractor.Constructor, new ConstructorExtractorImpl(project));
    selection.put(ExececutionExtractor.Method, new MethodExtractorImpl(project));

    targetMap.put(ExecutionTarget.Field, new FieldTargetImpl(project));
    targetMap.put(ExecutionTarget.Variable, new VariableTargetImpl(project));
  }

  public void runIntroduceMock(PsiParameter[] override, ExecutionTarget executionTarget)
      throws MultipleResultException, ExecutionTypeNotFoundException, ExpressionListNotFoundException,
      PsiTypeNotFoundException, ClassFromTypeNotFoundException, ClassFromExpressionNotFoundException {

    var expressionList = findPsiExpressionList();

    var executionType = findExecutionType(expressionList);

    var parameters = override.length == 0 ? getPsiParameters(expressionList, executionType) : override;

    var changeMap = Arrays.stream(expressionList.getExpressionTypes())
        .map(x -> x.equalsToText("null"))
        .collect(Collectors.toList());

    var mockExpressions = targetMap.get(executionTarget).createMockExpressions(parameters, changeMap);

    var variableNames = targetMap.get(executionTarget).extractVariableNames(mockExpressions);
    replaceNullValues(expressionList, variableNames, changeMap);

    var localVarAnchor = getAnchor(expressionList, executionType, executionTarget);

    writeExpressionsToCode(localVarAnchor, mockExpressions, changeMap);
  }

  private PsiElement getAnchor(PsiExpressionList expressionList, ExececutionExtractor executionType, ExecutionTarget executionTarget) {

    if (executionTarget == ExecutionTarget.Field) {
      PsiElement element = expressionList;

      for (int i = 0; i < 10; i++) {

        var psiExpressionStatement = PsiTreeUtil.getParentOfType(element, PsiClass.class);

        if (psiExpressionStatement != null) {
          return psiExpressionStatement;
        }

        element = element.getParent();
      }
      throw new RuntimeException("Could not find class");
    }

    return selection.get(executionType).findAnchor(expressionList);

  }

  private PsiParameter[] getPsiParameters(PsiExpressionList expressionList, ExececutionExtractor executionType) throws MultipleResultException, PsiTypeNotFoundException, ClassFromTypeNotFoundException, ClassFromExpressionNotFoundException {
    return selection.get(executionType).getPsiParameters(expressionList);
  }

  private ExececutionExtractor findExecutionType(PsiExpressionList expressionList) throws ExecutionTypeNotFoundException {
    return selection.entrySet().stream()
        .filter(x -> x.getValue().isType(expressionList))
        .map(Map.Entry::getKey)
        .findFirst()
        .orElseThrow(ExecutionTypeNotFoundException::new);
  }

  private void replaceNullValues(PsiExpressionList expressionList, List<String> variableNames, List<Boolean> changeMap) {

    var expressions = expressionList.getExpressions();

    WriteCommandAction.runWriteCommandAction(project, () -> {
      for (int i = 0; i < expressions.length; i++) {
        if (changeMap.get(i)) {
          expressions[i].replace(factory.createReferenceFromText(variableNames.get(i), null));
        }
      }
    });
  }

  private PsiExpressionList findPsiExpressionList() throws ExpressionListNotFoundException {
    int offset = editor.getCaretModel().getOffset();
    var element = psiFile.findElementAt(offset);

    var expressionList = PsiTreeUtil.getParentOfType(element, PsiExpressionList.class);
    if (expressionList != null) {
      return expressionList;
    }

    throw new ExpressionListNotFoundException();
  }

  private void writeExpressionsToCode(PsiElement targetAnchor, List<PsiElement> result, List<Boolean> changeMap) {
    WriteCommandAction.runWriteCommandAction(project, () -> {
      var whiteSpace = PsiParserFacade.SERVICE.getInstance(project).createWhiteSpaceFromText("\n");

      for (int i = result.size() - 1; i >= 0; i--) {
        if (changeMap.get(i)) {
          var element = result.get(i);
          element.addAfter(whiteSpace, null);
          element.addBefore(whiteSpace, null);
          targetAnchor.addAfter(element, null);
        }
      }

      new ReformatCodeProcessor(project, false).run();
    });
  }
}
