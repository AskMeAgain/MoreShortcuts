package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.google.common.base.Strings;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.*;
import java.util.stream.Collectors;

@Value
@Builder
public class MappingContainer {

  List<Mapping> mappings;
  String packageName;
  List<InputObjectContainer> inputObjects;
  PsiType outputType;

  @Override
  public String toString() {

    var textMappings = mappings.stream()
        .map(x -> LombokToMapStructTemplate.MAPPING_TEMPLATE.replace("$SOURCE", " source=\"" + x.getSource() + "\"")
            .replace("$TARGET", String.join(".", x.getTargets()))
            .replace("$CONSTANT", x.getConstant()))
        .collect(Collectors.joining("\n"));

    var collect = inputObjects.stream()
        .map(InputObjectContainer::toString)
        .collect(Collectors.joining(", "));

    return LombokToMapStructTemplate.TEMPLATE
        .replace("$IN_PACKAGE", !Strings.isNullOrEmpty(packageName) ? "package $PACKAGE;" : "")
        .replace("$PACKAGE", packageName)
        .replace("$INPUTS", collect)
        .replace("$OUTPUT_TYPE", outputType.getPresentableText())
        .replace("$MAPPINGS", textMappings);
  }
}
