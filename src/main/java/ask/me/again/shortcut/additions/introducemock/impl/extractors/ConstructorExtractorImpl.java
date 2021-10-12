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
      var localVar = PsiTreeUtil.getParentOfType(newExpression, PsiLocalVariable.class);
      if (localVar != null) {
        return true;
      }
      var assignmentExpression = PsiTreeUtil.getParentOfType(newExpression, PsiAssignmentExpression.class);
      if (assignmentExpression != null) {
        return true;
      }
    }
    return false;
  }

  @Override
  public PsiParameter[] getPsiParameters(PsiExpressionList expressionList) throws MultipleIntroduceMockResultException, ClassFromTypeNotFoundException, ClassFromExpressionNotFoundException {
    var newExpression = PsiTreeUtil.getParentOfType(expressionList, PsiNewExpression.class);
    var classString = getClassString(newExpression);
    var psiClass = PsiHelpers.getClassFromString(project, classString);

    var result = Arrays.stream(psiClass.getConstructors())
        .map(PsiMethod::getParameterList)
        .filter(x -> x.getParametersCount() == expressionList.getExpressionCount())
        .map(PsiParameterList::getParameters)
        .collect(Collectors.toList());

    if (result.size() > 1) {
      throw new MultipleIntroduceMockResultException(result);
    } else {
      return result.get(0);
    }
  }

  private String getClassString(PsiNewExpression newExpression) throws ClassFromExpressionNotFoundException {
    var localVar = PsiTreeUtil.getParentOfType(newExpression, PsiLocalVariable.class);
    if (localVar != null) {
      return localVar.getType().getCanonicalText();
    }

    var assignment = PsiTreeUtil.getParentOfType(newExpression, PsiAssignmentExpression.class);
    if (assignment != null) {
      return assignment.getType().getCanonicalText();
    }

    throw new ClassFromExpressionNotFoundException("Could not extract class");
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
      element = element.getParent();
    }
    throw new RuntimeException("Could not find parent");
  }

}
