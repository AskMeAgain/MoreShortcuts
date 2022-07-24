package io.github.askmeagain.more.shortcuts.mapstructbuilder.entities;

import com.esotericsoftware.kryo.io.Input;
import com.intellij.psi.PsiExpressionList;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.List;
import java.util.Set;

@Value
@Builder
public class SourceContainer {

  PsiExpressionList originalList;
  String sourceString;

  boolean externalMethod;
  boolean nestedMethodCall;
  String nestedMethodType;
  Set<InputObjectContainer> nestedMethodInputs;

}
