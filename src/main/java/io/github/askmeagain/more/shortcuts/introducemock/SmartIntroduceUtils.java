package io.github.askmeagain.more.shortcuts.introducemock;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import com.intellij.psi.util.PsiTreeUtil;
import io.github.askmeagain.more.shortcuts.commons.PsiHelpers;
import io.github.askmeagain.more.shortcuts.introducemock.entities.ExececutionExtractor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.intellij.openapi.ui.popup.JBPopupFactory.ActionSelectionAid.SPEEDSEARCH;

public class SmartIntroduceUtils {

  public static boolean isMethod(PsiExpressionList expressionList) {
    var methodReference = PsiTreeUtil.getPrevSiblingOfType(expressionList, PsiReferenceExpression.class);
    if (methodReference != null) {

      var result1 = PsiTreeUtil.getChildOfType(methodReference, PsiReferenceExpression.class);
      if (result1 != null) {
        return true;
      }

      var result2 = PsiTreeUtil.getChildOfType(methodReference, PsiMethodCallExpression.class);
      if (result2 != null) {
        return true;
      }

      var result3 = PsiTreeUtil.getChildOfType(methodReference, PsiNewExpression.class);
      if (result3 != null) {
        return true;
      }

      var result4 = PsiTreeUtil.getChildOfType(methodReference, PsiReferenceParameterList.class);
      if (result4 != null) {
        return true;
      }
    }
    return false;
  }

  public static boolean isConstructor(PsiExpressionList expressionList) {
    var newExpression = PsiTreeUtil.getParentOfType(expressionList, PsiNewExpression.class);
    if (newExpression != null) {
      return true;
    }
    return false;
  }

  public static <T extends PsiElement> T findRecursivelyInParent(PsiElement element, Class<T> seachFor) {
    while (true) {
      element = element.getParent();
      if (element == null) {
        return null;
      }
      if (element.getClass().isAssignableFrom(seachFor)) {
        return (T) element;
      }
    }

  }

  public static List<PsiParameter[]> getPsiParametersFromMethod(PsiExpressionList expressionList) {

    var method = ((PsiMethodCallExpressionImpl) expressionList.getParent()).resolveMethod();

    var psiClass = method.getContainingClass();

    return Arrays.stream(psiClass.getMethods())
        .filter(x -> x.getName().equals(method.getName()))
        .filter(x -> {
          if (!expressionList.isEmpty()) {
            return x.getParameterList().getParametersCount() == expressionList.getExpressionCount();
          }
          return true;
        })
        .map(x -> x.getParameterList().getParameters())
        .collect(Collectors.toList());
  }

  public static List<PsiParameter[]> getPsiParametersFromConstructor(PsiExpressionList expressionList) {
    var newExpression = PsiTreeUtil.getParentOfType(expressionList, PsiNewExpression.class);
    var psiClass = ((PsiClassReferenceType) newExpression.getType()).resolve();

    return Arrays.stream(psiClass.getConstructors())
        .map(PsiMethod::getParameterList)
        .filter(x -> {
          if (!expressionList.isEmpty()) {
            return x.getParametersCount() == expressionList.getExpressionCount();
          }
          return true;
        })
        .map(PsiParameterList::getParameters)
        .collect(Collectors.toList());
  }

  public static void createContextMenu(
      PsiExpressionList oldExpressionList,
      ExececutionExtractor execution,
      List<PsiParameter[]> parameterList,
      Editor editor,
      DataContext dataContext,
      @NotNull AnActionEvent e,
      Integer textOffset
  ) {
    if (parameterList.size() == 1) {
      new SmartIntroduceListDisplayAction(oldExpressionList, execution, parameterList.get(0), textOffset).actionPerformed(e);
    } else {
      var actionGroup = new DefaultActionGroup();

      parameterList.forEach(x -> actionGroup.add(new SmartIntroduceListDisplayAction(oldExpressionList, execution, x, textOffset)));

      var popup = JBPopupFactory.getInstance()
          .createActionGroupPopup(null, actionGroup, dataContext, SPEEDSEARCH, false);

      popup.showInBestPositionFor(editor);
    }
  }

  public static String toCamelCase(String s) {
    return Character.toLowerCase(s.charAt(0)) + s.substring(1);
  }
}
