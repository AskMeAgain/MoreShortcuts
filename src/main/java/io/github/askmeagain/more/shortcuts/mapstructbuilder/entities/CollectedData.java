package io.github.askmeagain.more.shortcuts.mapstructbuilder.entities;

import com.intellij.psi.PsiType;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Set;

@Value
@Builder
public class CollectedData {
  List<MapStructOverrideMethod> overrideMethods;

  List<MapStructMethod> mapStructMethodList;
  String packageName;
  Set<InputObjectContainer> inputObjects;
  PsiType outputType;
}
