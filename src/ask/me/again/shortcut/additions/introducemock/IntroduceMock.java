package ask.me.again.shortcut.additions.introducemock;

import ask.me.again.shortcut.additions.introducemock.helpers.ExecutionType;
import ask.me.again.shortcut.additions.introducemock.impl.ConstructorImpl;
import ask.me.again.shortcut.additions.introducemock.impl.MethodImpl;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static ask.me.again.shortcut.additions.introducemock.PsiHelpers.decapitalizeString;

public class IntroduceMock {

  private Project project;
  private Editor editor;
  private PsiElementFactory factory;
  private PsiFile psiFile;

  private StringBuilder stringBuilder;
  private final Map<ExecutionType, IntroduceMockImplementation> selection = new HashMap<>();

  public IntroduceMock(AnActionEvent e) {
    project = e.getProject();
    editor = e.getData(CommonDataKeys.EDITOR);
    psiFile = e.getData(CommonDataKeys.PSI_FILE);

    factory = JavaPsiFacade.getElementFactory(project);

    stringBuilder = new StringBuilder();

    selection.put(ExecutionType.Constructor, new ConstructorImpl(project, psiFile, stringBuilder));
    selection.put(ExecutionType.Method, new MethodImpl(project, psiFile, stringBuilder));
  }

  public void runIntroduceMock(PsiParameter[] override) throws MultipleResultException {
    var expressionList = findPsiExpressionList();

    var executionType = findExecutionType(expressionList);

    var parameters = override.length == 0 ? getPsiParameters(expressionList, executionType) : override;

    var mockExpressions = createMockExpressions(parameters);

    var changeMap = Arrays.stream(expressionList.getExpressionTypes())
        .map(x -> x.equalsToText("null"))
        .collect(Collectors.toList());

    var variableNames = PsiHelpers.extractVariableNames(mockExpressions);
    replaceNullValues(expressionList, variableNames, changeMap);

    var localVarAnchor = selection.get(executionType).findAnchor(expressionList);

    writeExpressionsToCode(localVarAnchor, mockExpressions, changeMap);
  }

  private PsiParameter[] getPsiParameters(PsiExpressionList expressionList, ExecutionType executionType) throws MultipleResultException {
    return selection.get(executionType).getPsiParameters(expressionList);
  }

  public String getLog() {
    return stringBuilder.toString();
  }

  private ExecutionType findExecutionType(PsiExpressionList expressionList) {
    return selection.entrySet().stream()
        .filter(x -> x.getValue().isType(expressionList))
        .map(Map.Entry::getKey)
        .findFirst()
        .get();
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

  private PsiExpressionList findPsiExpressionList() {
    int offset = editor.getCaretModel().getOffset();
    var element = psiFile.findElementAt(offset);

    var expressionList = PsiTreeUtil.getParentOfType(element, PsiExpressionList.class);
    if (expressionList != null) {
      return expressionList;
    }

    throw new RuntimeException("Could not find psiExpressionList");
  }

  private void writeExpressionsToCode(PsiElement targetAnchor, List<PsiElement> result, List<Boolean> changeMap) {
    WriteCommandAction.runWriteCommandAction(project, () -> {
      var whiteSpace = PsiParserFacade.SERVICE.getInstance(project).createWhiteSpaceFromText("\n");

      for (int i = result.size() - 1; i >= 0; i--) {
        if (changeMap.get(i)) {
          var element = result.get(i);
          element.addBefore(whiteSpace, null);
          element.addAfter(whiteSpace, null);
          targetAnchor.addAfter(element, null);
        }
      }

      new ReformatCodeProcessor(project, false).run();
    });
  }

  private List<PsiElement> createMockExpressions(PsiParameter[] parameterList) {
    var resultList = new ArrayList<PsiElement>();
    for (int i = 0; i < parameterList.length; i++) {

      var type = parameterList[i].getType();
      var presentableText = type.getPresentableText();
      var equalsCall = (PsiMethodCallExpression) factory.createExpressionFromText(String.format("Mockito.mock(%s.class)", presentableText), null);

      PsiType varType = factory.createTypeByFQClassName("var");
      var variableAssignment = factory.createVariableDeclarationStatement(decapitalizeString(presentableText), varType, equalsCall);

      resultList.add(variableAssignment);
    }

    return resultList;
  }


}
