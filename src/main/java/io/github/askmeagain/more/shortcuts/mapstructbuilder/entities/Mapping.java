package io.github.askmeagain.more.shortcuts.mapstructbuilder.entities;

import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Mapping {

  SourceContainer source;
  @Singular
  List<InputObjectContainer> inputObjects;

  @Singular
  @EqualsAndHashCode.Include
  List<PsiReferenceExpressionImpl> targets;
  String constant;

}
