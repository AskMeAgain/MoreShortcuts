package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.intellij.psi.PsiExpressionList;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SourceContainer {

  PsiExpressionList originalList;
  String sourceString;
  boolean externalMethod;

}
