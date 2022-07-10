package io.github.askmeagain.more.shortcuts.introducemock.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiParameter;

import java.util.List;

public interface IntroduceTarget {

  PsiElement createExpression(PsiParameter psiParameter);

  List<PsiElement> createMockExpressions(PsiParameter[] parameterList, List<Boolean> changeMap);

  List<String> extractVariableNames(List<PsiElement> result);

  void writeExpressionsToCode(PsiElement anchor, List<PsiElement> mockExpressions, List<Boolean> changeMap);

}
