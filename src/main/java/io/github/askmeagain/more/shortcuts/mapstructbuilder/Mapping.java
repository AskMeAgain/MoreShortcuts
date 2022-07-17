package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.intellij.psi.PsiType;
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
  List<String> targets;
  String constant;

}
