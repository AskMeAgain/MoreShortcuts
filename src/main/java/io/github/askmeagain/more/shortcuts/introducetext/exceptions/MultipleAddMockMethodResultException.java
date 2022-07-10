package io.github.askmeagain.more.shortcuts.introducetext.exceptions;

import com.intellij.psi.PsiMethod;

import java.util.List;

public class MultipleAddMockMethodResultException extends Exception {

  private final List<PsiMethod> psiMethodList;

  public MultipleAddMockMethodResultException(List<PsiMethod> psiMethodList) {
    this.psiMethodList = psiMethodList;
  }

  public List<PsiMethod> getPsiMethodList() {
    return psiMethodList;
  }
}
