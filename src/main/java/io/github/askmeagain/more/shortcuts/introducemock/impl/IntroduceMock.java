package io.github.askmeagain.more.shortcuts.introducemock.impl;

import io.github.askmeagain.more.shortcuts.commons.PsiHelpers;
import io.github.askmeagain.more.shortcuts.introducemock.entities.ExececutionExtractor;
import io.github.askmeagain.more.shortcuts.introducemock.entities.ExecutionTarget;
import io.github.askmeagain.more.shortcuts.introducemock.entities.MockType;
import io.github.askmeagain.more.shortcuts.introducemock.exceptions.*;
import io.github.askmeagain.more.shortcuts.introducemock.impl.extractors.ConstructorExtractorImpl;
import io.github.askmeagain.more.shortcuts.introducemock.impl.extractors.MethodExtractorImpl;
import io.github.askmeagain.more.shortcuts.introducemock.impl.targets.FieldTargetImpl;
import io.github.askmeagain.more.shortcuts.introducemock.impl.targets.VariableTargetImpl;
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
import java.util.stream.IntStream;

public class IntroduceMock {

  private final MockType mockType;
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
  private PsiClass mockTypeClass;

  private final AnActionEvent actionEvent;
  private PsiExpressionList expressionList;

  @SneakyThrows
  public IntroduceMock(AnActionEvent e, ExecutionTarget executionTarget, MockType mockType) {
    this.executionTarget = executionTarget;
    this.actionEvent = e;
    this.mockType = mockType;
  }

  public void analyze(PsiParameter[] override)
      throws MultipleIntroduceMockResultException, ExecutionTypeNotFoundException, ExpressionListNotFoundException,
      PsiTypeNotFoundException, ClassFromTypeNotFoundException, ClassFromExpressionNotFoundException {

    setup();

    expressionList = findPsiExpressionList();
    expressions = expressionList.getExpressions();

    var executionType = findExecutionType(expressionList);

    var parameters = override.length == 0 ? getPsiParameters(expressionList, executionType) : override;

    if (expressionList.isEmpty()) {
      changeMap = IntStream.range(0, parameters.length)
          .boxed()
          .map(x -> true)
          .collect(Collectors.toList());
    } else {
      changeMap = Arrays.stream(expressionList.getExpressionTypes())
          .map(x -> x.equalsToText("null"))
          .collect(Collectors.toList());
    }

    mockExpressions = targetMap.get(executionTarget).createMockExpressions(parameters, changeMap);

    variableNames = targetMap.get(executionTarget).extractVariableNames(mockExpressions);

    localVarAnchor = getAnchor(expressionList, executionType, executionTarget);
  }

  public void refactorCode() throws ClassFromTypeNotFoundException {

    if (mockExpressions == null || mockExpressions.isEmpty()) {
      return;
    }

    targetMap.get(executionTarget).writeExpressionsToCode(localVarAnchor, mockExpressions, changeMap);

    if (!expressionList.isEmpty()) {
      replaceNullValues(variableNames, changeMap, expressions);
    } else {
      variableNames.forEach(name -> expressionList.add(factory.createExpressionFromText(name, null)));
    }
    writeImport(executionTarget);

    new ReformatCodeProcessor(psiFile, false).run();
  }

  private void writeImport(ExecutionTarget executionTarget) {
    var importList = PsiTreeUtil.getChildOfType(psiFile, PsiImportList.class);
    if (importList != null) {
      if (executionTarget == ExecutionTarget.Variable) {
        importList.add(factory.createImportStatement(mockito));
      } else if (executionTarget == ExecutionTarget.Field) {
        importList.add(factory.createImportStatement(mockTypeClass));
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
        expressions[i].replace(factory.createExpressionFromText(variableNames.get(i), null));
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

    var mockTypeText = this.mockType == MockType.mock ? "Mock" : "Spy";
    mockTypeClass = PsiHelpers.getClassFromString(project, "org.mockito." + mockTypeText);

    selection.put(ExececutionExtractor.Constructor, new ConstructorExtractorImpl(project));
    selection.put(ExececutionExtractor.Method, new MethodExtractorImpl(project));

    targetMap.put(ExecutionTarget.Field, new FieldTargetImpl(psiFile, project, mockType));
    targetMap.put(ExecutionTarget.Variable, new VariableTargetImpl(psiFile, project, mockType));
  }
}
