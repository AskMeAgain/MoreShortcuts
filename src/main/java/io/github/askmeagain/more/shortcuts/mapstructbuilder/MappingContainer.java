package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.google.common.base.Strings;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiPolyadicExpression;
import com.intellij.psi.PsiType;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@Value
@Builder
public class MappingContainer {

  List<Mapping> overrideMethods;

  List<Mapping> mappings;
  String packageName;
  List<InputObjectContainer> inputObjects;
  PsiType outputType;

  @Override
  public String toString() {

    var textMappings = mappings.stream()
        .map(x -> LombokToMapStructTemplate.MAPPING_TEMPLATE.replace("$SOURCE", getSourceMappingString(x))
            .replace("$TARGET", String.join(".", x.getTargets()))
            .replace("$CONSTANT", x.getConstant()))
        .collect(Collectors.joining("\n"));

    var textOverrideMethods = overrideMethods.stream()
        .map(x -> LombokToMapStructTemplate.OVERRIDE_TEMPLATE.replace("$INPUT_TYPE", "abc")
            .replace("$OUTPUT_TYPE", getOutputType(x.getSource().getOriginalList()))
            .replace("$METHOD_NAME", getMappingMethodName(x))
            .replace("$CODE", x.getSource().getOriginalList().getText()))
        .collect(Collectors.joining("\n"));

    var collect = inputObjects.stream()
        .map(InputObjectContainer::toString)
        .collect(Collectors.joining(", "));

    return LombokToMapStructTemplate.TEMPLATE
        .replace("$IN_PACKAGE", !Strings.isNullOrEmpty(packageName) ? "package $PACKAGE;" : "")
        .replace("$PACKAGE", packageName)
        .replace("$INPUTS", collect)
        .replace("$OUTPUT_TYPE", outputType.getPresentableText())
        .replace("$MAPPINGS", textMappings)
        .replace("$OVERRIDE_METHODS", textOverrideMethods);
  }

  @NotNull
  private String getOutputType(PsiExpressionList expressionList) {
    var result = new ArrayList<String>();
    expressionList.accept(new JavaRecursiveElementVisitor() {
      @Override
      public void visitPolyadicExpression(PsiPolyadicExpression expression) {
        super.visitPolyadicExpression(expression);
        result.add(expression.getType().getPresentableText());
      }
    });

    return result.get(0);
  }

  private String getSourceMappingString(Mapping mapping) {
    if(mapping.getSource() == null){
      return "";
    } else if (mapping.getSource().isExternalMethod()) {
      return ", source = \"????????????\", qualifiedByName=\"" + getMappingMethodName(mapping) + "\"";
    } else {
      return ", source =\"" + mapping.getSource().getSourceString() + "\"";
    }
  }

  private String getMappingMethodName(Mapping mapping) {
    var methodName = mapping.getTargets().get(mapping.getTargets().size() - 1);

    return "get" + StringUtils.capitalize(methodName);
  }
}
