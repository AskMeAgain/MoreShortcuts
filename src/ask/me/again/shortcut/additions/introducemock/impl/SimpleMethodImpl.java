package ask.me.again.shortcut.additions.introducemock.impl;

import ask.me.again.shortcut.additions.introducemock.exceptions.MultipleResultException;
import ask.me.again.shortcut.additions.introducemock.exceptions.PsiTypeNotFoundException;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SimpleMethodImpl extends BaseImpl {

  public SimpleMethodImpl(Project project, PsiFile psiFile, StringBuilder stringBuilder) {
    super(project, psiFile, stringBuilder);
  }

  public boolean isType(PsiExpressionList expressionList) {
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
    }
    return false;
  }

  public PsiParameter[] getPsiParameters(PsiExpressionList expressionList) throws MultipleResultException, PsiTypeNotFoundException {
    var methodReference = PsiTreeUtil.getPrevSiblingOfType(expressionList, PsiReferenceExpression.class);
    var methodName = methodReference.getReferenceName();

    var psiType = getPsiType(methodReference);
    var psiClass = getClassFromType(psiType);

    var result = Arrays.stream(psiClass.getMethods())
        .filter(x -> x.getName().equals(methodName))
        .filter(x -> x.getParameterList().getParametersCount() == expressionList.getExpressionCount())
        .map(x -> x.getParameterList().getParameters())
        .collect(Collectors.toList());

    if (result.size() > 1) {
      throw new MultipleResultException(result);
    } else {
      return result.get(0);
    }
  }

  private PsiType getPsiType(PsiReferenceExpression methodReference) throws PsiTypeNotFoundException {
    var result1 = PsiTreeUtil.getChildOfType(methodReference, PsiReferenceExpression.class);
    if (result1 != null) {
      return result1.getType();
    }

    var result2 = PsiTreeUtil.getChildOfType(methodReference, PsiMethodCallExpression.class);
    if (result2 != null) {
      return result2.getType();
    }

    throw new PsiTypeNotFoundException();
  }

  public PsiElement findAnchor(PsiExpressionList expressionList) {
    PsiElement element = expressionList;
    for (int i = 0; i < 5; i++) {
      PsiElement psiExpressionStatement = PsiTreeUtil.getParentOfType(element, PsiExpressionStatement.class);
      if (psiExpressionStatement != null) {
        return psiExpressionStatement;
      }
      element = element.getParent();
    }
    throw new RuntimeException("Could not find parent");
  }
}