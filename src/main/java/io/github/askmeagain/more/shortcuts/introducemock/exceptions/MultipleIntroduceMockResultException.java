package io.github.askmeagain.more.shortcuts.introducemock.exceptions;

import com.intellij.psi.PsiParameter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MultipleIntroduceMockResultException extends Exception {

  @Getter
  private final List<PsiParameter[]> psiParametersList;

}
