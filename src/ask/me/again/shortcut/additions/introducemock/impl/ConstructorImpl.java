package ask.me.again.shortcut.additions.introducemock.impl;

import ask.me.again.shortcut.additions.introducemock.MultipleResultException;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ConstructorImpl extends BaseImpl {

  public ConstructorImpl(Project project, PsiFile psiFile, StringBuilder stringBuilder) {
    super(project, psiFile, stringBuilder);
  }

  public boolean isType(PsiExpressionList expressionList) {
    stringBuilder.append("\nConstructor: trying isType");
    var newExpression = PsiTreeUtil.getParentOfType(expressionList, PsiNewExpression.class);
    if (newExpression != null) {
      stringBuilder.append("\nConstructor: went 1 deep down");
      var localVar = PsiTreeUtil.getParentOfType(newExpression, PsiLocalVariable.class);
      if (localVar != null) {
        stringBuilder.append("\nConstructor: went 1 deep down again");
        return true;
      }
    }
    return false;
  }

  @Override
  public PsiParameter[] getPsiParameters(PsiExpressionList expressionList) throws MultipleResultException {
    var newExpression = PsiTreeUtil.getParentOfType(expressionList, PsiNewExpression.class);
    var localVar = PsiTreeUtil.getParentOfType(newExpression, PsiLocalVariable.class);
    var psiClass = getClassFromType(localVar.getType());

    var result = Arrays.stream(psiClass.getConstructors())
        .map(PsiMethod::getParameterList)
        .filter(x -> x.getParametersCount() == expressionList.getExpressionCount())
        .map(PsiParameterList::getParameters)
        .collect(Collectors.toList());

    if (result.size() > 1) {
      throw new MultipleResultException(result);
    } else {
      return result.get(0);
    }
  }

  @Override
  public PsiElement findAnchor(PsiExpressionList expressionList) {
    PsiElement element = expressionList;
    for (int i = 0; i < 5; i++) {
      var psiLocalVar = PsiTreeUtil.getParentOfType(element, PsiLocalVariable.class);
      if (psiLocalVar != null) {
        return psiLocalVar;
      }
      element = element.getParent();
    }
    throw new RuntimeException("Could not find parent");
  }

}
