package io.github.askmeagain.more.shortcuts.mapstructbuilder.entities;

import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.LombokToMapstructUtils;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class MapStructOverrideMethod {

  PsiExpressionList originalList;
  List<PsiReferenceExpressionImpl> targets;

  public String printOverrideMethods() {
    var inputTypes = String.join(",", LombokToMapstructUtils.getSpecificMappingInputs(originalList).stream()
        .map(y -> y.getType().getPresentableText() + " " + y.getVarName())
        .collect(Collectors.joining(", ")));

    return LombokToMapStructTemplate.OVERRIDE_TEMPLATE.replace("$METHOD_INPUT_TYPES", inputTypes)
        .replace("$OUTPUT_TYPE", LombokToMapstructUtils.getOutputType(this.getOriginalList()))
        .replace("$METHOD_NAME", getMappingMethodName())
        .replace("$CODE", trimBrackets());
  }

  private String getMappingMethodName() {
    var methodName = targets.stream()
        .map(PsiReferenceExpressionImpl::getReferenceName)
        .collect(Collectors.toList())
        .get(targets.size() - 1);

    return "get" + StringUtils.capitalize(methodName);
  }

  private String trimBrackets() {
    var text = originalList.getText();
    return text.substring(1, text.length() - 1);
  }
}
