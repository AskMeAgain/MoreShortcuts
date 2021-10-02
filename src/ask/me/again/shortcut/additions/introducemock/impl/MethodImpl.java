package ask.me.again.shortcut.additions.introducemock.impl;

import ask.me.again.shortcut.additions.introducemock.helpers.ExecutionType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;

public class MethodImpl extends BaseImpl {

  public MethodImpl(Project project, PsiFile psiFile, StringBuilder stringBuilder) {
    super(project, psiFile, stringBuilder);
  }

  public boolean isType(PsiExpressionList expressionList) {
    stringBuilder.append("\nMethod: Trying isType");
    var methodReference = PsiTreeUtil.getPrevSiblingOfType(expressionList, PsiReferenceExpression.class);
    if (methodReference != null) {
      stringBuilder.append("\n Method: Went 1 deep, found first method reference");
      var referenceExpression = PsiTreeUtil.getChildOfType(methodReference, PsiReferenceExpression.class);
      if (referenceExpression != null) {
        stringBuilder.append("\n Method: Went 1 deep again, found second reference");
        return true;
      }
    }
    return false;
  }

  public Pair<PsiParameter[], ExecutionType> getPsiParameters(PsiExpressionList expressionList) {
    var methodReference = PsiTreeUtil.getPrevSiblingOfType(expressionList, PsiReferenceExpression.class);
    var methodName = methodReference.getReferenceName();
    var referenceExpression = PsiTreeUtil.getChildOfType(methodReference, PsiReferenceExpression.class);

    var psiType = referenceExpression.getType();
    var psiClass = getClassFromType(psiType);

    return Arrays.stream(psiClass.getMethods()).filter(x -> x.getName().equals(methodName))
        .filter(x -> x.getParameterList().getParametersCount() == expressionList.getExpressionCount())
        .map(x -> x.getParameterList().getParameters())
        .findFirst()
        .map(x -> Pair.of(x, ExecutionType.Method))
        .get();
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
