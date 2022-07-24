package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.google.common.base.Strings;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiPolyadicExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import com.intellij.psi.search.GlobalSearchScope;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.InputObjectContainer;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.LombokToMapStructTemplate;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.Mapping;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.LombokToMapStructTemplate.MAPPING_METHOD_TEMPLATE;

@Value
@Builder
public class MappingResultService {

  Project project;
  List<Mapping> overrideMethods;

  Map<PsiType, Map<Mapping, Mapping>> mappingsByType;
  String packageName;
  List<InputObjectContainer> inputObjects;
  PsiType outputType;

  @Override
  public String toString() {

    var baseImports = new ArrayList<String>();

    baseImports.add("org.mapstruct.Mapper");
    baseImports.add("org.mapstruct.Mapping");

    var textOverrideMethods = overrideMethods.stream()
        .map(x -> LombokToMapStructTemplate.OVERRIDE_TEMPLATE.replace("$METHOD_INPUT_TYPES", String.join(",", getSpecificMappingInputs(x).stream()
                .map(y -> y.getType().getPresentableText() + " " + y.getVarName())
                .collect(Collectors.joining(", "))))
            .replace("$OUTPUT_TYPE", getOutputType(x.getSource().getOriginalList()))
            .replace("$METHOD_NAME", getMappingMethodName(x))
            .replace("$CODE", trimBrackets(x)))
        .collect(Collectors.joining("\n"));

    var inputs = inputObjects.stream()
        .map(InputObjectContainer::toString)
        .collect(Collectors.joining(", "));

    //fixing up null part
    var removed = mappingsByType.remove(null);
    for (var mapping : removed.values()) {
      mappingsByType.get(outputType).put(mapping, mapping);
    }

    Stream.of(
            inputObjects.stream().map(x -> x.getType().getCanonicalText()),
            Stream.of(outputType.getCanonicalText()),
            mappingsByType.keySet().stream()
                .map(PsiType::getCanonicalText)
        )
        .flatMap(Function.identity())
        .distinct()
        .forEach(baseImports::add);

    if (!textOverrideMethods.isEmpty()) {
      baseImports.add("org.mapstruct.Named");
    }

    var mainMapping = getMappingMethods(inputs);

    return LombokToMapStructTemplate.TEMPLATE
        .replace("$MAPPING_METHODS", String.join("\n", mainMapping))
        .replace("$IN_PACKAGE", !Strings.isNullOrEmpty(packageName) ? "package $PACKAGE;" : "")
        .replace("$PACKAGE", packageName)
        .replace("$OUTPUT_TYPE", outputType.getPresentableText())
        .replace("$IMPORTS", baseImports.stream().map(x -> "import " + x + ";").collect(Collectors.joining("\n")))
        .replace("$OVERRIDE_METHODS", textOverrideMethods);
  }

  @NotNull
  private String getMappingMethods(String collect) {

    var result = new ArrayList<String>();

    for (var method : mappingsByType.entrySet()) {

      var mappingAnnotation = method.getValue().values().stream()
          .map(x -> LombokToMapStructTemplate.MAPPING_TEMPLATE
              .replace("$SOURCE", getSourceMappingString(x))
              .replace("$TARGET", x.getTargets().stream()
                  .skip(x.getTargets().size() - 1)
                  .map(PsiReferenceExpressionImpl::getReferenceName)
                  .collect(Collectors.joining(".")))
              .replace("$CONSTANT", x.getConstant()))
          .collect(Collectors.joining("\n"));

      var outputTypeReplacement = method.getKey() == null ? "$OUTPUT_TYPE" : method.getKey().getPresentableText();

      var mainMapping = MAPPING_METHOD_TEMPLATE
          .replace("$MAPPINGS", mappingAnnotation)
          .replace("$OUTPUT_TYPE", outputTypeReplacement)
          .replace("$INPUTS", collect);

      result.add(mainMapping);

    }

    return String.join("\n", result);
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
    } else if (mapping.getSource().isNestedMethodCall()) {
      var shortInputObj = inputObjects.stream()
          .map(InputObjectContainer::getVarName)
          .distinct()
          .collect(Collectors.joining(", "));
      var methodInvocation = "map" + mapping.getSource().getNestedMethodType() + "(" + shortInputObj + ")";
      return ", expression=\"java(" + methodInvocation + ")\"";
    } else {
      return ", source =\"" + mapping.getSource().getSourceString() + "\"";
    }
  }

  private List<InputObjectContainer> getSpecificMappingInputs(Mapping mapping) {
    return new ArrayList<>(LombokToMapStructVisitor.getInputObjects(mapping.getSource().getOriginalList()));
  }

  private String getMappingMethodName(Mapping mapping) {
    var methodName = mapping.getTargets().stream()
        .map(PsiReferenceExpressionImpl::getReferenceName)
        .collect(Collectors.toList())
        .get(mapping.getTargets().size() - 1);

    return "get" + StringUtils.capitalize(methodName);
  }

  private String getMappingMethodNameNestedMapping(Mapping mapping) {
    var methodName = mapping.getTargets().stream()
        .map(PsiReferenceExpressionImpl::getType)
        .map(x -> x.getPresentableText())
        .collect(Collectors.toList())
        .get(mapping.getTargets().size() - 1);

    return "map" + StringUtils.capitalize(methodName);
  }
}
