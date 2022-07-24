package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.google.common.base.Strings;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiType;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.*;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Value
@Builder
public class MappingResultService {

  Project project;
  CollectedData collectedData;

  public String printResult() {

    var packageName = collectedData.getPackageName();
    var outputType = collectedData.getOutputType();
    var baseImports = new ArrayList<String>();

    baseImports.add("org.mapstruct.Mapper");
    baseImports.add("org.mapstruct.Mapping");

    var textOverrideMethods = collectedData.getOverrideMethods().stream()
        .map(MapStructOverrideMethod::printOverrideMethods)
        .collect(Collectors.joining("\n"));

    Stream.of(
            collectedData.getInputObjects().stream()
                .map(InputObjectContainer::getType),
            Stream.of(outputType),
            collectedData.getMapStructMethodList().stream()
                .map(MapStructMethod::getOutputType),
            collectedData.getMapStructMethodList().stream()
                .map(MapStructMethod::getInputs)
                .flatMap(Collection::stream)
                .map(InputObjectContainer::getType)
        )
        .flatMap(Function.identity())
        .map(PsiType::getCanonicalText)
        .distinct()
        .forEach(baseImports::add);

    if (!textOverrideMethods.isEmpty()) {
      baseImports.add("org.mapstruct.Named");
    }

    var mainMapping = getMappingMethods();

    return LombokToMapStructTemplate.TEMPLATE
        .replace("$MAPPING_METHODS", String.join("\n", mainMapping))
        .replace("$IN_PACKAGE", !Strings.isNullOrEmpty(packageName) ? "package $PACKAGE;" : "")
        .replace("$PACKAGE", packageName)
        .replace("$OUTPUT_TYPE", outputType.getPresentableText())
        .replace("$IMPORTS", baseImports.stream().map(x -> "import " + x + ";").collect(Collectors.joining("\n")))
        .replace("$OVERRIDE_METHODS", textOverrideMethods);
  }

  @NotNull
  private String getMappingMethods() {
    return collectedData.getMapStructMethodList().stream()
        .map(x -> x.printMethod(collectedData.getOutputType()))
        .collect(Collectors.joining("\n"));
  }

  public String getMapperName() {
    return collectedData.getOutputType().getPresentableText() + "Mapper.java";
  }
}