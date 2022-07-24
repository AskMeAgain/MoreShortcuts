package io.github.askmeagain.more.shortcuts.mapstructbuilder.entities;

import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiType;
import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Value
@Builder
public class SourceContainer {

  PsiExpressionList originalList;
  String sourceString;

  boolean externalMethod;
  boolean nestedMethodCall;
  PsiType nestedMethodType;
  Set<InputObjectContainer> nestedMethodInputs;

}
