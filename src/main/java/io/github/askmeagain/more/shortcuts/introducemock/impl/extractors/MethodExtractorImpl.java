package io.github.askmeagain.more.shortcuts.introducemock.impl.extractors;

import io.github.askmeagain.more.shortcuts.commons.PsiHelpers;
import io.github.askmeagain.more.shortcuts.introducemock.exceptions.ClassFromTypeNotFoundException;
import io.github.askmeagain.more.shortcuts.introducemock.exceptions.MultipleIntroduceMockResultException;
import io.github.askmeagain.more.shortcuts.introducemock.exceptions.PsiTypeNotFoundException;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
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

  public PsiParameter[] getPsiParameters(PsiExpressionList expressionList) throws MultipleIntroduceMockResultException, PsiTypeNotFoundException, ClassFromTypeNotFoundException {
    var methodReference = PsiTreeUtil.getPrevSiblingOfType(expressionList, PsiReferenceExpression.class);
    java.util.List<PsiParameter[]> result = getPsiParametersFromReference(expressionList, methodReference);

    if (result.size() > 1) {
      throw new MultipleIntroduceMockResultException(result);
    } else {
      return result.get(0);
    }
  }

  @NotNull
  public List<PsiParameter[]> getPsiParametersFromReference(PsiExpressionList expressionList, PsiReferenceExpression methodReference)
      throws PsiTypeNotFoundException, ClassFromTypeNotFoundException {
    var methodName = methodReference.getReferenceName();

    var classNameFromReference = getClassNameFromReference(methodReference);
    var psiClass = PsiHelpers.getClassFromString(project, classNameFromReference);

    var result = Arrays.stream(psiClass.getMethods())
        .filter(x -> x.getName().equals(methodName))
        .filter(x -> {
          if (!expressionList.isEmpty()) {
            return x.getParameterList().getParametersCount() == expressionList.getExpressionCount();
          }
          return true;
        })
        .map(x -> x.getParameterList().getParameters())
        .collect(Collectors.toList());
    return result;
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

    var result3 = PsiTreeUtil.getChildOfType(methodReference, PsiNewExpression.class);
    if (result3 != null) {
      return result3.getType().getCanonicalText();
    }

    var result4 = PsiTreeUtil.getChildOfType(methodReference, PsiReferenceParameterList.class);
    if (result4 != null) {
      throw new UnsupportedOperationException("Varargs not supported sadly :(");
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
