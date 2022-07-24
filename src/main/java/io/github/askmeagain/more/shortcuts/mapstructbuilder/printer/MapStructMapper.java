package io.github.askmeagain.more.shortcuts.mapstructbuilder.printer;

import com.google.common.base.Strings;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiType;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.*;
import lombok.Builder;
import lombok.Value;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Value
@Builder
public class MapStructMapper {

  Project project;
  CollectedData collectedData;

  public String printResult() {

    var packageName = collectedData.getPackageName();
    var outputType = collectedData.getOutputType();
    var baseImports = new ArrayList<String>();

    baseImports.add("org.mapstruct.Mapper");
    baseImports.add("org.mapstruct.Mapping");
    baseImports.add("org.mapstruct.factory.Mappers");

    var textOverrideMethods = collectedData.getOverrideMethods().stream()
        .map(MapStructOverrideMethod::printOverrideMethods)
        .collect(Collectors.joining("\n"));

    Stream.of(
            Stream.of(outputType),
            collectedData.getInputObjects().stream()
                .map(InputObjectContainer::getType),
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

    var mappingMethods = collectedData.getMapStructMethodList().stream()
        .map(x1 -> x1.printMethod(collectedData.getOutputType()))
        .collect(Collectors.joining("\n"));

    return MAPPER_TEMPLATE
        .replace("$MAPPING_METHODS", String.join("\n", mappingMethods))
        .replace("$IN_PACKAGE", !Strings.isNullOrEmpty(packageName) ? "package $PACKAGE;" : "")
        .replace("$PACKAGE", packageName)
        .replace("$OUTPUT_TYPE", outputType.getPresentableText())
        .replace("$IMPORTS", baseImports.stream()
            .map(x -> "import " + x + ";")
            .collect(Collectors.joining("\n")))
        .replace("$OVERRIDE_METHODS", textOverrideMethods);
  }

  public String getMapperName() {
    return collectedData.getOutputType().getPresentableText() + "Mapper.java";
  }

  private static String MAPPER_TEMPLATE = "" +
      "$IN_PACKAGE\n" +
      "\n" +
      "$IMPORTS" +
      "\n" +
      "\n" +
      "@Mapper\n" +
      "public interface $OUTPUT_TYPEMapper {\n" +
      "\n" +
      "  $OUTPUT_TYPEMapper INSTANCE = Mappers.getMapper($OUTPUT_TYPEMapper.class);\n" +
      "\n" +
      "$MAPPING_METHODS" +
      "\n" +
      "$OVERRIDE_METHODS" +
      "}";
}
