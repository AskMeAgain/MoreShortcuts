package ask.me.again.shortcut.additions.introducemock.exceptions;

import com.intellij.psi.PsiParameter;

import java.util.List;

public class MultipleIntroduceMockResultException extends Exception {

  private final List<PsiParameter[]> psiParametersList;

  public MultipleIntroduceMockResultException(List<PsiParameter[]> psiParametersList) {
    this.psiParametersList = psiParametersList;
  }

  public List<PsiParameter[]> getPsiParametersList() {
    return psiParametersList;
  }
}
