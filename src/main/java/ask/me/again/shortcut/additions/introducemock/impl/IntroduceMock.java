package ask.me.again.shortcut.additions.introducemock.impl;

import ask.me.again.shortcut.additions.PsiHelpers;
import ask.me.again.shortcut.additions.introducemock.entities.ExececutionExtractor;
import ask.me.again.shortcut.additions.introducemock.entities.ExecutionTarget;
import ask.me.again.shortcut.additions.introducemock.exceptions.*;
import ask.me.again.shortcut.additions.introducemock.impl.extractors.ConstructorExtractorImpl;
import ask.me.again.shortcut.additions.introducemock.impl.extractors.MethodExtractorImpl;
import ask.me.again.shortcut.additions.introducemock.impl.targets.FieldTargetImpl;
import ask.me.again.shortcut.additions.introducemock.impl.targets.VariableTargetImpl;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IntroduceMock {

  private Project project;
  private Editor editor;
  private PsiElementFactory factory;
  private PsiFile psiFile;

  private final Map<ExececutionExtractor, IntroduceExtractors> selection = new HashMap<>();
  private final Map<ExecutionTarget, IntroduceTarget> targetMap = new HashMap<>();
  private List<String> variableNames;
  private List<Boolean> changeMap;

  private final ExecutionTarget executionTarget;
  private PsiElement localVarAnchor;
  private List<PsiElement> mockExpressions;
  private PsiExpression[] expressions;
  private PsiClass mockito;
  private PsiClass mock;

  private final AnActionEvent actionEvent;
  private PsiExpressionList expressionList;

  @SneakyThrows
  public IntroduceMock(AnActionEvent e, ExecutionTarget executionTarget) {
    this.executionTarget = executionTarget;
    this.actionEvent = e;
  }

  public void analyze(PsiParameter[] override)
      throws MultipleIntroduceMockResultException, ExecutionTypeNotFoundException, ExpressionListNotFoundException,
      PsiTypeNotFoundException, ClassFromTypeNotFoundException, ClassFromExpressionNotFoundException {

    setup();

    expressionList = findPsiExpressionList();
    expressions = expressionList.getExpressions();

    var executionType = findExecutionType(expressionList);

    var parameters = override.length == 0 ? getPsiParameters(expressionList, executionType) : override;

    changeMap = Arrays.stream(expressionList.getExpressionTypes())
        .map(x -> x.equalsToText("null"))
        .collect(Collectors.toList());

    mockExpressions = targetMap.get(executionTarget).createMockExpressions(parameters, changeMap);

    variableNames = targetMap.get(executionTarget).extractVariableNames(mockExpressions);

    localVarAnchor = getAnchor(expressionList, executionType, executionTarget);
  }

  public void refactorCode() throws ClassFromTypeNotFoundException {

    if (mockExpressions == null || mockExpressions.isEmpty()) {
      return;
    }

    targetMap.get(executionTarget).writeExpressionsToCode(localVarAnchor, mockExpressions, changeMap);
    replaceNullValues(variableNames, changeMap, expressions);
    writeImport(executionTarget);

    new ReformatCodeProcessor(psiFile, false).run();
  }

  private void writeImport(ExecutionTarget executionTarget) {
    var importList = PsiTreeUtil.getChildOfType(psiFile, PsiImportList.class);
    if (importList != null) {
      if (executionTarget == ExecutionTarget.Variable) {
        importList.add(factory.createImportStatement(mockito));
      } else if (executionTarget == ExecutionTarget.Field) {
        importList.add(factory.createImportStatement(mock));
      }
    }
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

  private PsiParameter[] getPsiParameters(PsiExpressionList expressionList, ExececutionExtractor executionType) throws MultipleIntroduceMockResultException, PsiTypeNotFoundException, ClassFromTypeNotFoundException, ClassFromExpressionNotFoundException {
    return selection.get(executionType).getPsiParameters(expressionList);
  }

  private ExececutionExtractor findExecutionType(PsiExpressionList expressionList) throws ExecutionTypeNotFoundException {
    return selection.entrySet().stream()
        .filter(x -> x.getValue().isType(expressionList))
        .map(Map.Entry::getKey)
        .findFirst()
        .orElseThrow(ExecutionTypeNotFoundException::new);
  }

  private void replaceNullValues(List<String> variableNames, List<Boolean> changeMap, PsiExpression[] expressions) {
    for (int i = 0; i < expressions.length; i++) {
      if (changeMap.get(i)) {
        var expressionFromText = factory.createExpressionFromText(variableNames.get(i), null);

        var clazz = factory.createClass("Integer");
        clazz.setName(variableNames.get(i));
        var expression = factory.createReferenceExpression(clazz);

        expressionList.getExpressions()[i].replace(expression);
      }
    }
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

  private void setup() throws ClassFromTypeNotFoundException {
    project = actionEvent.getProject();
    editor = actionEvent.getData(CommonDataKeys.EDITOR);
    psiFile = actionEvent.getData(CommonDataKeys.PSI_FILE);

    factory = JavaPsiFacade.getElementFactory(project);

    mockito = PsiHelpers.getClassFromString(project, "org.mockito.Mockito");
    mock = PsiHelpers.getClassFromString(project, "org.mockito.Mock");

    selection.put(ExececutionExtractor.Constructor, new ConstructorExtractorImpl(project));
    selection.put(ExececutionExtractor.Method, new MethodExtractorImpl(project));

    targetMap.put(ExecutionTarget.Field, new FieldTargetImpl(psiFile, project));
    targetMap.put(ExecutionTarget.Variable, new VariableTargetImpl(psiFile, project));
  }
}
