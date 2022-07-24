package io.github.askmeagain.more.shortcuts.mapstructbuilder.printer;

import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiPolyadicExpression;
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.LombokToMapstructUtils;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class MapStructOverrideMethod {

  PsiExpressionList originalList;
  List<PsiReferenceExpressionImpl> targets;

  private static String OVERRIDE_TEMPLATE = "  @Named(\"$METHOD_NAME\")\n" +
      "  default $OUTPUT_TYPE $METHOD_NAME ($METHOD_INPUT_TYPES){\n" +
      "    return $CODE;\n  }" +
      "\n";

  public String printOverrideMethods() {
    var inputTypes = String.join(",", LombokToMapstructUtils.getSpecificMappingInputs(originalList).stream()
        .map(y -> y.getType().getPresentableText() + " " + y.getVarName())
        .collect(Collectors.joining(", ")));

    return OVERRIDE_TEMPLATE.replace("$METHOD_INPUT_TYPES", inputTypes)
        .replace("$OUTPUT_TYPE", getOutputType())
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

  private String getOutputType() {
    var result = new ArrayList<String>();
    originalList.accept(new JavaRecursiveElementVisitor() {
      @Override
      public void visitPolyadicExpression(PsiPolyadicExpression expression) {
        super.visitPolyadicExpression(expression);
        result.add(expression.getType().getPresentableText());
      }
    });

    return result.get(0);
  }
}
