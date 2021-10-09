package ask.me.again.shortcut.additions.introducemock.impl;

import ask.me.again.shortcut.additions.introducemock.IntroduceMockImplementation;
import ask.me.again.shortcut.additions.introducemock.entities.ExecutionType;
import ask.me.again.shortcut.additions.introducemock.entities.PsiHelpers;
import ask.me.again.shortcut.additions.introducemock.exceptions.*;
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

import static ask.me.again.shortcut.additions.introducemock.entities.PsiHelpers.decapitalizeString;

public class IntroduceMock {

  private final Project project;
  private final Editor editor;
  private final PsiElementFactory factory;
  private final PsiFile psiFile;

  private final StringBuilder stringBuilder;
  private final Map<ExecutionType, IntroduceMockImplementation> selection = new HashMap<>();

  public IntroduceMock(AnActionEvent e) {
    project = e.getProject();
    editor = e.getData(CommonDataKeys.EDITOR);
    psiFile = e.getData(CommonDataKeys.PSI_FILE);

    factory = JavaPsiFacade.getElementFactory(project);

    stringBuilder = new StringBuilder();

    selection.put(ExecutionType.Constructor, new SimpleConstructorImpl(project, psiFile, stringBuilder));
    selection.put(ExecutionType.Method, new SimpleMethodImpl(project, psiFile, stringBuilder));
  }

  public void runIntroduceMock(PsiParameter[] override) throws MultipleResultException, ExecutionTypeNotFoundException, ExpressionListNotFoundException, PsiTypeNotFoundException, ClassFromTypeNotFoundException {
    var expressionList = findPsiExpressionList();

    var executionType = findExecutionType(expressionList);

    var parameters = override.length == 0 ? getPsiParameters(expressionList, executionType) : override;

    var changeMap = Arrays.stream(expressionList.getExpressionTypes())
        .map(x -> x.equalsToText("null"))
        .collect(Collectors.toList());

    var mockExpressions = createMockExpressions(parameters, changeMap);

    var variableNames = PsiHelpers.extractVariableNames(mockExpressions);
    replaceNullValues(expressionList, variableNames, changeMap);

    var localVarAnchor = selection.get(executionType).findAnchor(expressionList);

    writeExpressionsToCode(localVarAnchor, mockExpressions, changeMap);
  }

  private PsiParameter[] getPsiParameters(PsiExpressionList expressionList, ExecutionType executionType) throws MultipleResultException, PsiTypeNotFoundException, ClassFromTypeNotFoundException {
    return selection.get(executionType).getPsiParameters(expressionList);
  }

  private ExecutionType findExecutionType(PsiExpressionList expressionList) throws ExecutionTypeNotFoundException {
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

  private List<PsiElement> createMockExpressions(PsiParameter[] parameterList, List<Boolean> changeMap) {
    var resultList = new ArrayList<PsiElement>();

    for (int i = 0; i < parameterList.length; i++) {
      if (changeMap.get(i)) {
        PsiParameter psiParameter = parameterList[i];

        var presentableText = psiParameter.getType().getPresentableText();
        var equalsCall = (PsiMethodCallExpression) factory.createExpressionFromText(String.format("Mockito.mock(%s.class)", presentableText), null);

        var varType = factory.createTypeByFQClassName("var");
        var variableAssignment = factory.createVariableDeclarationStatement(decapitalizeString(presentableText), varType, equalsCall);

        resultList.add(variableAssignment);
      } else {
        resultList.add(null);
      }
    }

    return resultList;
  }

}
