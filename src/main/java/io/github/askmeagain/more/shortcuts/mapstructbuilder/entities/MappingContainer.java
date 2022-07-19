package io.github.askmeagain.more.shortcuts.mapstructbuilder.entities;

import com.google.common.base.Strings;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiPolyadicExpression;
import com.intellij.psi.PsiType;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.LombokToMapStructVisitor;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    var baseImports = new ArrayList<String>();

    baseImports.add("org.mapstruct.Mapper");
    baseImports.add("org.mapstruct.Mapping");

    var textMappings = mappings.stream()
        .map(x -> LombokToMapStructTemplate.MAPPING_TEMPLATE.replace("$SOURCE", getSourceMappingString(x))
            .replace("$TARGET", String.join(".", x.getTargets()))
            .replace("$CONSTANT", x.getConstant()))
        .collect(Collectors.joining("\n"));

    var textOverrideMethods = overrideMethods.stream()
        .map(x -> LombokToMapStructTemplate.OVERRIDE_TEMPLATE.replace("$METHOD_INPUT_TYPES", String.join(",", getSpecificMappingInputs(x).stream()
                .map(y -> y.getType().getPresentableText() + " " + y.getVarName())
                .collect(Collectors.joining(", "))))
            .replace("$OUTPUT_TYPE", getOutputType(x.getSource().getOriginalList()))
            .replace("$METHOD_NAME", getMappingMethodName(x))
            .replace("$CODE", trimBrackets(x)))
        .collect(Collectors.joining("\n"));

    var collect = inputObjects.stream()
        .map(InputObjectContainer::toString)
        .collect(Collectors.joining(", "));

    Stream.concat(
            inputObjects.stream().map(x -> x.getType().getCanonicalText()),
            Stream.of(outputType.getCanonicalText())
        ).distinct()
        .forEach(baseImports::add);

    if (!textOverrideMethods.isEmpty()) {
      baseImports.add("org.mapstruct.Named");
    }

    return LombokToMapStructTemplate.TEMPLATE
        .replace("$IN_PACKAGE", !Strings.isNullOrEmpty(packageName) ? "package $PACKAGE;" : "")
        .replace("$PACKAGE", packageName)
        .replace("$INPUTS", collect)
        .replace("$OUTPUT_TYPE", outputType.getPresentableText())
        .replace("$MAPPINGS", textMappings)
        .replace("$IMPORTS", baseImports.stream().map(x -> "import " + x + ";").collect(Collectors.joining("\n")))
        .replace("$OVERRIDE_METHODS", textOverrideMethods);
  }

  private String trimBrackets(Mapping x) {
    var text = x.getSource().getOriginalList().getText();
    return text.substring(1, text.length() - 1);
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
    if (mapping.getSource() == null) {
      return "";
    } else if (mapping.getSource().isExternalMethod()) {
      var inputObjects = getSpecificMappingInputs(mapping);
      if (inputObjects.size() != 1) {
        var shortInputObj = inputObjects.stream()
            .map(InputObjectContainer::getVarName)
            .collect(Collectors.joining(", "));
        var methodInvocation = getMappingMethodName(mapping) + "(" + shortInputObj + ")";
        return ", expression=\"java(" + methodInvocation + ")\"";
      } else {
        var sourceName = inputObjects.get(0).getVarName();
        return ", source = \"" + sourceName + "\", qualifiedByName=\"" + getMappingMethodName(mapping) + "\"";
      }
    } else {
      return ", source =\"" + mapping.getSource().getSourceString() + "\"";
    }
  }

  private List<InputObjectContainer> getSpecificMappingInputs(Mapping mapping) {
    return new ArrayList<>(LombokToMapStructVisitor.getInputObjects(mapping.getSource().getOriginalList()));
  }

  private String getMappingMethodName(Mapping mapping) {
    var methodName = mapping.getTargets().get(mapping.getTargets().size() - 1);

    return "get" + StringUtils.capitalize(methodName);
  }
}
