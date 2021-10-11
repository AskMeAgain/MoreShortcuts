package ask.me.again.shortcut.additions.introducemock.impl.extractors;

import ask.me.again.shortcut.additions.introducemock.entities.PsiHelpers;
import ask.me.again.shortcut.additions.introducemock.exceptions.ClassFromTypeNotFoundException;
import ask.me.again.shortcut.additions.introducemock.exceptions.MultipleIntroduceMockResultException;
import ask.me.again.shortcut.additions.introducemock.exceptions.PsiTypeNotFoundException;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MethodExtractorImpl extends ExtractorBase {

  public MethodExtractorImpl(Project project) {
    super(project);
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

  public PsiParameter[] getPsiParameters(PsiExpressionList expressionList) throws MultipleIntroduceMockResultException, PsiTypeNotFoundException, ClassFromTypeNotFoundException {
    var methodReference = PsiTreeUtil.getPrevSiblingOfType(expressionList, PsiReferenceExpression.class);
    var methodName = methodReference.getReferenceName();

    var classNameFromReference = getClassNameFromReference(methodReference);
    var psiClass = PsiHelpers.getClassFromString(project, classNameFromReference);

    var result = Arrays.stream(psiClass.getMethods())
        .filter(x -> x.getName().equals(methodName))
        .filter(x -> x.getParameterList().getParametersCount() == expressionList.getExpressionCount())
        .map(x -> x.getParameterList().getParameters())
        .collect(Collectors.toList());

    if (result.size() > 1) {
      throw new MultipleIntroduceMockResultException(result);
    } else {
      return result.get(0);
    }
  }

  private String getClassNameFromReference(PsiReferenceExpression methodReference) throws PsiTypeNotFoundException {
    var result1 = PsiTreeUtil.getChildOfType(methodReference, PsiReferenceExpression.class);
    if (result1 != null) {
      return result1.getType() == null ? result1.getQualifiedName() : result1.getType().getCanonicalText();
    }

    var result2 = PsiTreeUtil.getChildOfType(methodReference, PsiMethodCallExpression.class);
    if (result2 != null) {
      return result2.getType().getCanonicalText();
    }

    throw new PsiTypeNotFoundException();
  }

  public PsiElement findAnchor(PsiExpressionList expressionList) {
    PsiElement element = expressionList;

    for (int i = 0; i < 5; i++) {
      var psiExpressionStatement = PsiTreeUtil.getParentOfType(element, PsiExpressionStatement.class);
      var psiExpressionStatement2 = PsiTreeUtil.getParentOfType(element, PsiLocalVariable.class);

      if (psiExpressionStatement != null) {
        return psiExpressionStatement;
      }

      if (psiExpressionStatement2 != null) {
        return psiExpressionStatement2;
      }

      element = element.getParent();
    }

    throw new RuntimeException("Could not find parent");
  }
}
