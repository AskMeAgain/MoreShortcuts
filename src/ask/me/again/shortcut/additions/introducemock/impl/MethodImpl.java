package ask.me.again.shortcut.additions.introducemock.impl;

import ask.me.again.shortcut.additions.introducemock.MultipleResultException;
import ask.me.again.shortcut.additions.introducemock.helpers.ExecutionType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MethodImpl extends BaseImpl {

  public MethodImpl(Project project, PsiFile psiFile, StringBuilder stringBuilder) {
    super(project, psiFile, stringBuilder);
  }

  public boolean isType(PsiExpressionList expressionList) {
    var methodReference = PsiTreeUtil.getPrevSiblingOfType(expressionList, PsiReferenceExpression.class);
    if (methodReference != null) {
      var referenceExpression = PsiTreeUtil.getChildOfType(methodReference, PsiReferenceExpression.class);
      if (referenceExpression != null) {
        return true;
      }
    }
    return false;
  }

  public PsiParameter[] getPsiParameters(PsiExpressionList expressionList) throws MultipleResultException {
    var methodReference = PsiTreeUtil.getPrevSiblingOfType(expressionList, PsiReferenceExpression.class);
    var methodName = methodReference.getReferenceName();
    var referenceExpression = PsiTreeUtil.getChildOfType(methodReference, PsiReferenceExpression.class);

    var psiType = referenceExpression.getType();
    var psiClass = getClassFromType(psiType);

    var result = Arrays.stream(psiClass.getMethods()).filter(x -> x.getName().equals(methodName))
        .filter(x -> x.getParameterList().getParametersCount() == expressionList.getExpressionCount())
        .map(x -> x.getParameterList().getParameters())
        .collect(Collectors.toList());

    if (result.size() > 1) {
      throw new MultipleResultException(result);
    } else {
      return result.get(0);
    }
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
