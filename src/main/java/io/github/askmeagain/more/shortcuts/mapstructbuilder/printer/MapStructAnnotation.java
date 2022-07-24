package io.github.askmeagain.more.shortcuts.mapstructbuilder.printer;

import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.LombokToMapstructUtils;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.InputObjectContainer;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.SourceContainer;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

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

  private static String MAPPING_TEMPLATE = "  @Mapping(target = \"$TARGET\"$SOURCE$CONSTANT)";

  public String printAnnotation() {
    return MAPPING_TEMPLATE
        .replace("$SOURCE", getSourceMappingString())
        .replace("$TARGET", this.getTargets().stream()
            .skip(this.getTargets().size() - 1)
            .map(PsiReferenceExpressionImpl::getReferenceName)
            .collect(Collectors.joining(".")))
        .replace("$CONSTANT", this.getConstant());
  }

  private String getSourceMappingString() {
    if (this.getSource() == null) {
      return "";
    } else if (this.getSource().isExternalMethod()) {
      var inputObjects = LombokToMapstructUtils.getSpecificMappingInputs(source.getOriginalList());
      var mappingMethodName = getMappingMethodName();
      if (inputObjects.size() != 1) {
        var shortInputObj = inputObjects.stream()
            .map(InputObjectContainer::getVarName)
            .collect(Collectors.joining(", "));
        var methodInvocation = mappingMethodName + "(" + shortInputObj + ")";
        return ", expression=\"java(" + methodInvocation + ")\"";
      } else {
        var sourceName = inputObjects.get(0).getVarName();
        return ", source = \"" + sourceName + "\", qualifiedByName=\"" + mappingMethodName + "\"";
      }
    } else if (this.getSource().isNestedMethodCall()) {
      var shortInputObj = this.getInputObjects()
          .stream()
          .map(InputObjectContainer::getVarName)
          .distinct()
          .collect(Collectors.joining(", "));
      if (this.getInputObjects().size() != 1) {
        var methodInvocation = "map" + this.getSource().getNestedMethodType().getPresentableText() + "(" + shortInputObj + ")";
        return ", expression=\"java(" + methodInvocation + ")\"";
      } else {
        return ", source = \"" + shortInputObj + "\"";
      }
    } else {
      return ", source =\"" + this.getSource().getSourceString() + "\"";
    }
  }

  private String getMappingMethodName() {
    var methodName = targets.stream()
        .map(PsiReferenceExpressionImpl::getReferenceName)
        .collect(Collectors.toList())
        .get(targets.size() - 1);

    return "get" + StringUtils.capitalize(methodName);
  }
}
