package io.github.askmeagain.more.shortcuts.introducemock.impl;

import io.github.askmeagain.more.shortcuts.introducemock.exceptions.ClassFromExpressionNotFoundException;
import io.github.askmeagain.more.shortcuts.introducemock.exceptions.ClassFromTypeNotFoundException;
import io.github.askmeagain.more.shortcuts.introducemock.exceptions.MultipleIntroduceMockResultException;
import io.github.askmeagain.more.shortcuts.introducemock.exceptions.PsiTypeNotFoundException;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiParameter;

public interface IntroduceExtractors {

  PsiParameter[] getPsiParameters(PsiExpressionList expressionList) throws MultipleIntroduceMockResultException, PsiTypeNotFoundException, ClassFromTypeNotFoundException, ClassFromExpressionNotFoundException;

  PsiElement findAnchor(PsiExpressionList expressionList);

  boolean isType(PsiExpressionList expressionList);

}
