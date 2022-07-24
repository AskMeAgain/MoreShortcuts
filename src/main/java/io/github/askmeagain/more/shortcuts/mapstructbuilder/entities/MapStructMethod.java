package io.github.askmeagain.more.shortcuts.mapstructbuilder.entities;

import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.LombokToMapstructUtils;
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
  List<Mapping> mappings;

  public String toString(PsiType alternativeOutput) {
    var mappingAnnotation = mappings.stream()
        .map(Mapping::toString)
        .collect(Collectors.joining("\n"));

    var inputMappings = inputs.stream()
        .map(InputObjectContainer::toString)
        .collect(Collectors.joining(", "));

    var replacement = Optional.ofNullable(outputType)
        .map(PsiType::getPresentableText)
        .orElse(alternativeOutput.getPresentableText());

    return LombokToMapStructTemplate.MAPPING_METHOD_TEMPLATE
        .replace("$MAPPINGS", mappingAnnotation)
        .replace("$OUTPUT_TYPE", replacement)
        .replace("$INPUTS", inputMappings);
  }

  public MapStructMethod merge(MapStructMethod other) {
    return this.toBuilder()
        .mappings(other.getMappings())
        .inputs(other.getInputs())
        .build();
  }
}
