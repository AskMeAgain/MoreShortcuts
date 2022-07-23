package io.github.askmeagain.more.shortcuts.mapstructbuilder.entities;

import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Mapping {

  SourceContainer source;
  List<InputObjectContainer> inputObjects;
  @Singular
  List<PsiReferenceExpressionImpl> targets;
  String constant;

}
