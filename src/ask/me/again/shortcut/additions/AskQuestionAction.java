package ask.me.again.shortcut.additions;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AskQuestionAction extends AnAction {
  @Override
  public void update(AnActionEvent e) {
    // Get required data keys
    final Project project = e.getProject();
    final Editor editor = e.getData(CommonDataKeys.EDITOR);

    // Set visibility only in case of existing project and editor and if a selection exists
    e.getPresentation().setEnabledAndVisible(project != null && editor != null);
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    var builder = new StringBuilder();
    final Project project = e.getProject();

    PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);

    final Editor editor = e.getData(CommonDataKeys.EDITOR);

    int offset = editor.getCaretModel().getOffset();
    PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

    //all the magic
    findLocalVarExpression(psiFile.findElementAt(offset), localVar -> {
      var classType = getClassFromType(project, psiFile, localVar.getType());

      PsiParameter[] parameters = getPsiParameters(classType, builder);

      var result = createMockExpressions(parameters, factory, builder, project);

      var variableNames = result.stream()
          .map(x -> PsiTreeUtil.getChildOfType(x, PsiLocalVariable.class))
          .map(x -> x.getName()).collect(Collectors.toList());

      replaceNullValues(localVar, builder, factory, project, variableNames);
      writeExpressionsToCode(builder, project, localVar, result);
    });

    Messages.showMessageDialog(e.getProject(), builder.toString(), "PSI Info", null);
  }

  private void replaceNullValues(PsiLocalVariable localVar, StringBuilder builder, PsiElementFactory factory, Project project, List<String> variableNames) {
    var childrenOfType = PsiTreeUtil.getChildOfType(localVar, PsiNewExpression.class);
    if (childrenOfType != null) {
      builder.append("Found correct new expression child!\n");

      var expressionList = PsiTreeUtil.getChildOfType(childrenOfType, PsiExpressionList.class);
      if (expressionList != null) {
        builder.append("Found correct reference parameter\n");
        var expressions = expressionList.getExpressions();

        WriteCommandAction.runWriteCommandAction(project, () -> {
          for (int i = 0; i < expressions.length; i++) {
            builder.append("Iterating over expressions\n");
            PsiExpression x = expressions[i];
            //if (x.getType().equalsToText("null")) {
            expressions[i].replace(factory.createReferenceFromText(variableNames.get(i), null));
            //}
          }
        });
      }
    }
    builder.append("End of replacing null values");
  }

  private void findLocalVarExpression(PsiElement element, Consumer<PsiLocalVariable> consumer) {
    if (element != null) {

      var expressionList = PsiTreeUtil.getParentOfType(element, PsiExpressionList.class);
      if (expressionList != null) {

        var newExpression = PsiTreeUtil.getParentOfType(expressionList, PsiNewExpression.class);
        if (newExpression != null) {

          var localVar = PsiTreeUtil.getParentOfType(newExpression, PsiLocalVariable.class);
          if (localVar != null) {
            consumer.accept(localVar);
          }
        }
      }
    }
  }

  private void writeExpressionsToCode(StringBuilder builder, Project project, PsiLocalVariable localVar, List<PsiElement> result) {
    WriteCommandAction.runWriteCommandAction(project, () -> {
      var whiteSpace = PsiParserFacade.SERVICE.getInstance(project).createWhiteSpaceFromText("\n");

      result.forEach(x -> {
        x.addBefore(whiteSpace, null);
        x.addAfter(whiteSpace, null);
        localVar.addAfter(x, null);
      });

      new ReformatCodeProcessor(project, false).run();
    });
  }

  @Nullable
  private PsiClass getClassFromType(Project project, PsiFile psiFile, PsiType type) {
    return JavaPsiFacade.getInstance(project)
        .findClass(type.getCanonicalText(), GlobalSearchScope.fileScope(psiFile));
  }

  private PsiParameter[] getPsiParameters(PsiClass classType, StringBuilder builder) {
    PsiMethod resultConstructorMethod = null;
    builder.append("Trying to find correct constructor method");
    var constructors = classType.getConstructors();
    for (PsiMethod constructor : constructors) {

      if (constructor.getParameterList().getParametersCount() == 2) {
        resultConstructorMethod = constructor;
        break;
      }
    }

    return resultConstructorMethod.getParameterList().getParameters();
  }

  private List<PsiElement> createMockExpressions(PsiParameter[] expressionList, PsiElementFactory factory, StringBuilder builder, Project project) {
    var resultList = new ArrayList<PsiElement>();
    for (int i = 0; i < expressionList.length; i++) {

      try {
        var type = expressionList[i].getType();
        var presentableText = type.getPresentableText();
        builder.append("Presentable text: " + presentableText + "\n");
        var equalsCall = (PsiMethodCallExpression) factory.createExpressionFromText(String.format("Mockito.mock(%s.class)", presentableText), null);

        PsiType var2 = factory.createTypeByFQClassName("var");
        PsiDeclarationStatement variableAssignment = factory.createVariableDeclarationStatement(decapitalizeString(presentableText), var2, equalsCall);
        builder.append("Creating method for type: " + type);
        builder.append("\n");

        resultList.add(variableAssignment);

      } catch (Exception e) {
        builder.append("Exception: " + e.getMessage() + "\n");
      }
    }

    return resultList;
  }

  public static String decapitalizeString(String string) {
    return string == null || string.isEmpty() ? "" : Character.toLowerCase(string.charAt(0)) + string.substring(1);
  }
}
