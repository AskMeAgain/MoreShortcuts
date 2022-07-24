package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.google.common.base.Strings;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiPolyadicExpression;
import com.intellij.psi.PsiType;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.InputObjectContainer;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.LombokToMapStructTemplate;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.MapStructMethod;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.Mapping;
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
  List<Mapping> overrideMethods;

  List<MapStructMethod> mapStructMethodList;
  String packageName;
  Set<InputObjectContainer> inputObjects;
  PsiType outputType;

  @Override
  public String toString() {

    var baseImports = new ArrayList<String>();

    baseImports.add("org.mapstruct.Mapper");
    baseImports.add("org.mapstruct.Mapping");

    var textOverrideMethods = overrideMethods.stream()
        .map(x -> LombokToMapStructTemplate.OVERRIDE_TEMPLATE.replace("$METHOD_INPUT_TYPES", String.join(",", LombokToMapstructUtils.getSpecificMappingInputs(x).stream()
                .map(y -> y.getType().getPresentableText() + " " + y.getVarName())
                .collect(Collectors.joining(", "))))
            .replace("$OUTPUT_TYPE", getOutputType(x.getSource().getOriginalList()))
            .replace("$METHOD_NAME", LombokToMapstructUtils.getMappingMethodName(x))
            .replace("$CODE", trimBrackets(x)))
        .collect(Collectors.joining("\n"));

    Stream.of(
            inputObjects.stream()
                .map(InputObjectContainer::getType),
            Stream.of(outputType),
            mapStructMethodList.stream()
                .map(MapStructMethod::getOutputType),
            mapStructMethodList.stream()
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
    return mapStructMethodList.stream()
        .map(x -> x.toString(outputType))
        .collect(Collectors.joining("\n"));
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
}
