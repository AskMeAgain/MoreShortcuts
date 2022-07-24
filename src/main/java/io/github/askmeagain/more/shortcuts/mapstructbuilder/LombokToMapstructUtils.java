package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.InputObjectContainer;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.MapStructMethod;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.Mapping;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class LombokToMapstructUtils {

  public static String getMappingMethodName(Mapping mapping) {
    var methodName = mapping.getTargets().stream()
        .map(PsiReferenceExpressionImpl::getReferenceName)
        .collect(Collectors.toList())
        .get(mapping.getTargets().size() - 1);

    return "get" + StringUtils.capitalize(methodName);
  }

  public static String getMappingMethodNameNestedMapping(Mapping mapping) {
    var methodName = mapping.getTargets().stream()
        .map(PsiReferenceExpressionImpl::getType)
        .map(x -> x.getPresentableText())
        .collect(Collectors.toList())
        .get(mapping.getTargets().size() - 1);

    return "map" + StringUtils.capitalize(methodName);
  }

  public static List<InputObjectContainer> getSpecificMappingInputs(Mapping mapping) {
    return new ArrayList<>(LombokToMapStructVisitor.getInputObjects(mapping.getSource().getOriginalList()));
  }

  public static String getSourceMappingString(Mapping mapping, Set<InputObjectContainer> inputObjects2) {
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
      var shortInputObj = inputObjects2.stream()
          .map(InputObjectContainer::getVarName)
          .distinct()
          .collect(Collectors.joining(", "));
      var methodInvocation = "map" + mapping.getSource().getNestedMethodType() + "(" + shortInputObj + ")";
      return ", expression=\"java(" + methodInvocation + ")\"";
    } else {
      return ", source =\"" + mapping.getSource().getSourceString() + "\"";
    }
  }

  public static List<MapStructMethod> transformToMapstructMethodList(Map<PsiType, Map<Mapping, Mapping>> mappings, PsiType alternativeOutputType) {
    var result = new HashMap<MapStructMethod, MapStructMethod>();
    for (var kv : mappings.entrySet()) {

      var inputs = kv.getValue().values()
          .stream()
          .map(Mapping::getInputObjects)
          .flatMap(Collection::stream)
          .collect(Collectors.toSet());

      var output = kv.getKey() == null ? alternativeOutputType : kv.getKey();

      var method = MapStructMethod.builder()
          .outputType(output)
          .mappings(new ArrayList<>(kv.getValue().values()))
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
}
