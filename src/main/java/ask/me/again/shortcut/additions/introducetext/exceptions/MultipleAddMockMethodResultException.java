package ask.me.again.shortcut.additions.introducetext.exceptions;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;

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
