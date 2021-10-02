package ask.me.again.shortcut.additions.introducemock.impl;

import ask.me.again.shortcut.additions.introducemock.helpers.ExecutionType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang3.tuple.Pair;

public class ConstructorImpl extends BaseImpl {

  public ConstructorImpl(Project project, PsiFile psiFile) {
    super(project, psiFile);
  }

  @Override
  public Pair<PsiParameter[], ExecutionType> getPsiParameters(PsiExpressionList expressionList) {
    var newExpression = PsiTreeUtil.getParentOfType(expressionList, PsiNewExpression.class);
    if (newExpression != null) {

      var localVar = PsiTreeUtil.getParentOfType(newExpression, PsiLocalVariable.class);
      if (localVar != null) {
        var psiClass = getClassFromType(localVar.getType());
        var parameters = getPsiParametersFromConstructor(psiClass, expressionList.getExpressionCount());
        return Pair.of(parameters, ExecutionType.Constructor);
      }
    }

    return null;
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

  private PsiParameter[] getPsiParametersFromConstructor(PsiClass classType, int typeLookupList) {
    PsiMethod resultConstructorMethod = null;

    var constructors = classType.getConstructors();
    for (var psiMethod : constructors) {

      if (psiMethod.getParameterList().getParametersCount() == typeLookupList) {
        resultConstructorMethod = psiMethod;
        break;
      }
    }

    return resultConstructorMethod.getParameterList().getParameters();
  }
}
