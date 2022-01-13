package ask.me.again.shortcut.additions.introducemock.impl.extractors;

import ask.me.again.shortcut.additions.PsiHelpers;
import ask.me.again.shortcut.additions.introducemock.exceptions.ClassFromExpressionNotFoundException;
import ask.me.again.shortcut.additions.introducemock.exceptions.ClassFromTypeNotFoundException;
import ask.me.again.shortcut.additions.introducemock.exceptions.MultipleIntroduceMockResultException;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ConstructorExtractorImpl extends ExtractorBase {

  public ConstructorExtractorImpl(Project project) {
    super(project);
  }

  public boolean isType(PsiExpressionList expressionList) {
    var newExpression = PsiTreeUtil.getParentOfType(expressionList, PsiNewExpression.class);
    if (newExpression != null) {
      return true;
    }
    return false;
  }

  @Override
  public PsiParameter[] getPsiParameters(PsiExpressionList expressionList) throws MultipleIntroduceMockResultException, ClassFromTypeNotFoundException, ClassFromExpressionNotFoundException {
    var newExpression = PsiTreeUtil.getParentOfType(expressionList, PsiNewExpression.class);
    var classString = newExpression.getType().getCanonicalText();
    var psiClass = PsiHelpers.getClassFromString(project, classString);

    var result = Arrays.stream(psiClass.getConstructors())
        .map(PsiMethod::getParameterList)
        .filter(x -> {
          if (!expressionList.isEmpty()) {
            return x.getParametersCount() == expressionList.getExpressionCount();
          }
          return true;
        })
        .map(PsiParameterList::getParameters)
        .collect(Collectors.toList());

    if (result.size() > 1) {
      throw new MultipleIntroduceMockResultException(result);
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

      var assignmentExpression = PsiTreeUtil.getParentOfType(element, PsiAssignmentExpression.class);
      if (assignmentExpression != null) {
        return assignmentExpression;
      }

      var expressionStatement = PsiTreeUtil.getParentOfType(element, PsiExpressionStatement.class);
      if (expressionStatement != null) {
        return expressionStatement;
      }

      element = element.getParent();
    }
    throw new RuntimeException("Could not find parent");
  }

}
