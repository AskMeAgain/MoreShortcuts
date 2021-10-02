package ask.me.again.shortcut.additions.introducemock;

import ask.me.again.shortcut.additions.introducemock.helpers.ExecutionType;
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
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
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

    try {
      findLocalVarExpression(psiFile.findElementAt(offset), expressionList -> {
        var psiTypes = findTypesFromCursor(expressionList);

        var parameters = getPsiParameters(expressionList, psiTypes);

        var result = createMockExpressions(parameters.getLeft());

        var shortDict = new HashMap<String, Integer>();
        var variableNames = extractVariableNames(result, shortDict);

        var changeMap = Arrays.stream(psiTypes).map(x -> x.equalsToText("null"))
            .collect(Collectors.toList());

        replaceNullValues(expressionList, variableNames, changeMap);
        var localVarAnchor = findInsertionAnchor(expressionList, parameters.getRight());
        //TODO find localAnchor for method variant
        writeExpressionsToCode(localVarAnchor, result, changeMap);
      });
    } catch (Exception eee) {

    }
    var message = stringBuilder.toString();
    if (!message.isEmpty()) {
      Messages.showMessageDialog(e.getProject(), stringBuilder.toString(), "PSI Info", null);
    }
  }

  private PsiElement findInsertionAnchor(PsiExpressionList expressionList, ExecutionType executionType) {
    PsiElement element = expressionList;
    if (executionType == ExecutionType.Constructor) {
      for (int i = 0; i < 5; i++) {
        var psiLocalVar = PsiTreeUtil.getParentOfType(element, PsiLocalVariable.class);
        if (psiLocalVar != null) {
          return psiLocalVar;
        }
        element = element.getParent();
      }
      throw new RuntimeException("Could not find parent");
    } else if (executionType == ExecutionType.Method) {
      for (int i = 0; i < 5; i++) {
        PsiElement psiExpressionStatement = PsiTreeUtil.getParentOfType(element, PsiExpressionStatement.class);
        if (psiExpressionStatement != null) {
          return psiExpressionStatement;
        }
        element = element.getParent();
      }
      throw new RuntimeException("Could not find parent");
    }

    throw new NotImplementedException("This is not implemented");
  }

  @NotNull
  private Pair<PsiParameter[], ExecutionType> getPsiParameters(PsiExpressionList expressionList, PsiType[] psiTypes) {

    //constructor
    var newExpression = PsiTreeUtil.getParentOfType(expressionList, PsiNewExpression.class);
    if (newExpression != null) {

      var localVar = PsiTreeUtil.getParentOfType(newExpression, PsiLocalVariable.class);
      if (localVar != null) {
        var psiClass = getClassFromType(localVar.getType());
        var parameters = getPsiParametersFromConstructor(psiClass, psiTypes);
        return Pair.of(parameters, ExecutionType.Constructor);
      }
    }

    //method
    var methodReference = PsiTreeUtil.getPrevSiblingOfType(expressionList, PsiReferenceExpression.class);
    var methodName = methodReference.getReferenceName();
    var referenceExpression = PsiTreeUtil.getChildOfType(methodReference, PsiReferenceExpression.class);

    var psiType = referenceExpression.getType();

    var psiClass = getClassFromType(psiType);

    return Arrays.stream(psiClass.getMethods()).filter(x -> x.getName().equals(methodName))
        .filter(x -> x.getParameterList().getParametersCount() == psiTypes.length)
        .map(x -> x.getParameterList().getParameters())
        .findFirst()
        .map(x -> Pair.of(x, ExecutionType.Method))
        .get();
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

  private void findLocalVarExpression(PsiElement element, Consumer<PsiExpressionList> consumer) {
    if (element != null) {
      var expressionList = PsiTreeUtil.getParentOfType(element, PsiExpressionList.class);
      if (expressionList != null) {
        consumer.accept(expressionList);
      }
    }
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
