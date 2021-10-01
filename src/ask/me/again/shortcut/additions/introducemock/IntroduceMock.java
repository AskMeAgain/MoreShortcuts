package ask.me.again.shortcut.additions.introducemock;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class IntroduceMock extends AnAction {

  private Project project;
  private Editor editor;
  private PsiElementFactory factory;
  private PsiFile psiFile;
  private StringBuilder stringBuilder;

  private void init(AnActionEvent e) {
    stringBuilder = new StringBuilder();
    project = e.getProject();
    editor = e.getData(CommonDataKeys.EDITOR);
    psiFile = e.getData(CommonDataKeys.PSI_FILE);

    factory = JavaPsiFacade.getElementFactory(project);
  }

  @Override
  public void actionPerformed(AnActionEvent e) {

    init(e);

    int offset = editor.getCaretModel().getOffset();

    findLocalVarExpression(psiFile.findElementAt(offset), (expressionList, localVar) -> {
      var psiClass = getClassFromType(localVar.getType());

      var psiTypes = findTypesFromCursor(expressionList);

      var parameters = getPsiParametersFromConstructor(psiClass, psiTypes);

      var result = createMockExpressions(parameters);

      var shortDict = new HashMap<String, Integer>();
      var variableNames = extractVariableNames(result, shortDict);

      var changeMap = Arrays.stream(psiTypes).map(x -> x.equalsToText("null"))
          .collect(Collectors.toList());

      replaceNullValues(expressionList, variableNames, changeMap);
      writeExpressionsToCode(localVar, result, changeMap);
    });

    var message = stringBuilder.toString();
    if (!message.isEmpty()) {
      Messages.showMessageDialog(e.getProject(), stringBuilder.toString(), "PSI Info", null);
    }
  }

  @NotNull
  private List<String> extractVariableNames(List<PsiElement> result, HashMap<String, Integer> shortDict) {
    return result.stream()
        .map(x -> PsiTreeUtil.getChildOfType(x, PsiLocalVariable.class))
        .map(x -> {
          if (!shortDict.containsKey(x.getName())) {
            shortDict.put(x.getName(), 1);
          } else {
            var integer = shortDict.get(x.getName());
            shortDict.put(x.getName(), integer + 1);
            x.setName(x.getName() + integer);
          }
          return x;
        })
        .map(PsiLocalVariable::getName)
        .collect(Collectors.toList());
  }

  private PsiType[] findTypesFromCursor(PsiExpressionList expressionList) {
    return expressionList.getExpressionTypes();
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

  private void findLocalVarExpression(PsiElement element, BiConsumer<PsiExpressionList, PsiLocalVariable> consumer) {
    if (element != null) {

      var expressionList = PsiTreeUtil.getParentOfType(element, PsiExpressionList.class);
      if (expressionList != null) {

        var newExpression = PsiTreeUtil.getParentOfType(expressionList, PsiNewExpression.class);
        if (newExpression != null) {

          var localVar = PsiTreeUtil.getParentOfType(newExpression, PsiLocalVariable.class);
          if (localVar != null) {
            consumer.accept(expressionList, localVar);
          }
        }
      }
    }
  }

  private void writeExpressionsToCode(PsiLocalVariable localVar, List<PsiElement> result, List<Boolean> changeMap) {
    WriteCommandAction.runWriteCommandAction(project, () -> {
      var whiteSpace = PsiParserFacade.SERVICE.getInstance(project).createWhiteSpaceFromText("\n");

      for (int i = result.size() - 1; i >= 0; i--) {
        if (changeMap.get(i)) {
          var element = result.get(i);
          element.addBefore(whiteSpace, null);
          element.addAfter(whiteSpace, null);
          localVar.addAfter(element, null);
        }
      }

      new ReformatCodeProcessor(project, false).run();
    });
  }

  @Nullable
  private PsiClass getClassFromType(PsiType type) {
    return JavaPsiFacade.getInstance(project)
        .findClass(type.getCanonicalText(), GlobalSearchScope.fileScope(psiFile));
  }

  private PsiParameter[] getPsiParametersFromConstructor(PsiClass classType, PsiType[] typeLookupList) {
    PsiMethod resultConstructorMethod = null;

    var constructors = classType.getConstructors();
    for (var psiMethod : constructors) {

      if (psiMethod.getParameterList().getParametersCount() == typeLookupList.length) {
        resultConstructorMethod = psiMethod;
        break;
      }
    }

    return resultConstructorMethod.getParameterList().getParameters();
  }

  private List<PsiElement> createMockExpressions(PsiParameter[] parameterList) {
    var resultList = new ArrayList<PsiElement>();
    for (int i = 0; i < parameterList.length; i++) {

      if (parameterList[i] == null) {
        resultList.add(null);
      } else {
        var type = parameterList[i].getType();
        var presentableText = type.getPresentableText();
        var equalsCall = (PsiMethodCallExpression) factory.createExpressionFromText(String.format("Mockito.mock(%s.class)", presentableText), null);

        PsiType varType = factory.createTypeByFQClassName("var");
        PsiDeclarationStatement variableAssignment = factory.createVariableDeclarationStatement(decapitalizeString(presentableText), varType, equalsCall);

        resultList.add(variableAssignment);
      }
    }

    return resultList;
  }

  public static String decapitalizeString(String string) {
    return string == null || string.isEmpty() ? "" : Character.toLowerCase(string.charAt(0)) + string.substring(1);
  }
}
