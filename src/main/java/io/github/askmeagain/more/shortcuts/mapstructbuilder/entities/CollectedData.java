package io.github.askmeagain.more.shortcuts.mapstructbuilder.entities;

import com.intellij.psi.PsiType;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.printer.MapStructMethod;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.printer.MapStructOverrideMethod;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Set;

@Value
@Builder
public class CollectedData {
  String packageName;
  Set<InputObjectContainer> inputObjects;
  List<MapStructOverrideMethod> overrideMethods;
  Set<MapStructMethod> mapStructMethodList;
  PsiType outputType;
}
