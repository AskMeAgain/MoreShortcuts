package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiPolyadicExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import com.intellij.psi.search.GlobalSearchScope;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.InputObjectContainer;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.MapStructAnnotation;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.MapStructMethod;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class LombokToMapstructUtils {

  public static PsiType resolveBuilder(PsiType type, Project project) {
    var presentableText = type.getPresentableText();
    var replacedName = type.getCanonicalText().replaceAll("." + presentableText + "$", "");
    return PsiType.getTypeByName(replacedName, project, GlobalSearchScope.allScope(project));
  }

  public static String getMappingMethodName(MapStructAnnotation mapStructAnnotation) {
    var methodName = mapStructAnnotation.getTargets().stream()
        .map(PsiReferenceExpressionImpl::getReferenceName)
        .collect(Collectors.toList())
        .get(mapStructAnnotation.getTargets().size() - 1);

    return "get" + StringUtils.capitalize(methodName);
  }

  public static List<InputObjectContainer> getSpecificMappingInputs(PsiExpressionList mapStructAnnotation) {
    return new ArrayList<>(LombokToMapStructVisitor.getInputObjects(mapStructAnnotation));
  }

  public static String getSourceMappingString(MapStructAnnotation mapStructAnnotation) {
    if (mapStructAnnotation.getSource() == null) {
      return "";
    } else if (mapStructAnnotation.getSource().isExternalMethod()) {
      var inputObjects = getSpecificMappingInputs(mapStructAnnotation.getSource().getOriginalList());
      if (inputObjects.size() != 1) {
        var shortInputObj = inputObjects.stream()
            .map(InputObjectContainer::getVarName)
            .collect(Collectors.joining(", "));
        var methodInvocation = getMappingMethodName(mapStructAnnotation) + "(" + shortInputObj + ")";
        return ", expression=\"java(" + methodInvocation + ")\"";
      } else {
        var sourceName = inputObjects.get(0).getVarName();
        return ", source = \"" + sourceName + "\", qualifiedByName=\"" + getMappingMethodName(mapStructAnnotation) + "\"";
      }
    } else if (mapStructAnnotation.getSource().isNestedMethodCall()) {
      var shortInputObj = mapStructAnnotation.getInputObjects()
          .stream()
          .map(InputObjectContainer::getVarName)
          .distinct()
          .collect(Collectors.joining(", "));
      if (mapStructAnnotation.getInputObjects().size() != 1) {
        var methodInvocation = "map" + mapStructAnnotation.getSource().getNestedMethodType().getPresentableText() + "(" + shortInputObj + ")";
        return ", expression=\"java(" + methodInvocation + ")\"";
      } else {
        return ", source = \"" + shortInputObj + "\"";
      }
    } else {
      return ", source =\"" + mapStructAnnotation.getSource().getSourceString() + "\"";
    }
  }

  public static List<MapStructMethod> transformToMapstructMethodList(
      Map<PsiType, Map<MapStructAnnotation, MapStructAnnotation>> mappings,
      PsiType alternativeOutputType
  ) {
    var result = new HashMap<MapStructMethod, MapStructMethod>();
    for (var kv : mappings.entrySet()) {

      var inputs = kv.getValue().values()
          .stream()
          .map(MapStructAnnotation::getInputObjects)
          .flatMap(Collection::stream)
          .collect(Collectors.toSet());

      var output = kv.getKey() == null ? alternativeOutputType : kv.getKey();

      var method = MapStructMethod.builder()
          .outputType(output)
          .mapStructAnnotations(new ArrayList<>(kv.getValue().values()))
          .inputs(inputs)
          .build();

      if (result.containsKey(method)) {
        var existing = result.get(method);
        var newMethod = existing.merge(method);
        result.put(newMethod, newMethod);
      } else {
        result.put(method, method);
      }
    }

    return new ArrayList<>(result.values());
  }

  public static String getOutputType(PsiExpressionList expressionList) {
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
