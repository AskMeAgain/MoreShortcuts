package io.github.askmeagain.more.shortcuts.mapstructbuilder.entities;

import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.LombokToMapstructUtils;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MapStructAnnotation {

  @Singular
  @EqualsAndHashCode.Include
  List<PsiReferenceExpressionImpl> targets;
  @Singular
  List<InputObjectContainer> inputObjects;

  SourceContainer source;
  String constant;

  public String printAnnotation() {
    return MAPPING_TEMPLATE
        .replace("$SOURCE", LombokToMapstructUtils.getSourceMappingString(this))
        .replace("$TARGET", this.getTargets().stream()
            .skip(this.getTargets().size() - 1)
            .map(PsiReferenceExpressionImpl::getReferenceName)
            .collect(Collectors.joining(".")))
        .replace("$CONSTANT", this.getConstant());
  }

  private static String MAPPING_TEMPLATE = "  @Mapping(target = \"$TARGET\"$SOURCE$CONSTANT)";

}
