package io.github.askmeagain.more.shortcuts.mapstructbuilder.printer;

import com.intellij.psi.PsiType;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.InputObjectContainer;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.Value;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MapStructMethod {

  @EqualsAndHashCode.Include
  PsiType outputType;
  @Singular
  Set<InputObjectContainer> inputs;
  @Singular
  List<MapStructAnnotation> mapStructAnnotations;

  public String printMethod(PsiType alternativeOutput) {
    var mappingAnnotation = mapStructAnnotations.stream()
        .map(MapStructAnnotation::printAnnotation)
        .collect(Collectors.joining("\n"));

    var inputMappings = inputs.stream()
        .map(InputObjectContainer::toString)
        .collect(Collectors.joining(", "));

    var replacement = Optional.ofNullable(outputType)
        .map(PsiType::getPresentableText)
        .orElse(alternativeOutput.getPresentableText());

    return MAPPING_METHOD_TEMPLATE
        .replace("$MAPPINGS", mappingAnnotation)
        .replace("$OUTPUT_TYPE", replacement)
        .replace("$INPUTS", inputMappings);
  }

  public MapStructMethod merge(MapStructMethod other) {
    return this.toBuilder()
        .mapStructAnnotations(other.getMapStructAnnotations())
        .inputs(other.getInputs())
        .build();
  }

  private static String MAPPING_METHOD_TEMPLATE = "$MAPPINGS\n  $OUTPUT_TYPE map$OUTPUT_TYPE($INPUTS);\n";
}
