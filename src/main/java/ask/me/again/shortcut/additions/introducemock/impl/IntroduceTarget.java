package ask.me.again.shortcut.additions.introducemock.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiParameter;

import java.util.List;

public interface IntroduceTarget {

  PsiElement createExpression(PsiParameter psiParameter);

  List<PsiElement> createMockExpressions(PsiParameter[] parameterList, List<Boolean> changeMap);

  List<String> extractVariableNames(List<PsiElement> result);

  void writeExpressionsToCode(PsiElement anchor, List<PsiElement> expressionList, List<Boolean> changeMap);

}
