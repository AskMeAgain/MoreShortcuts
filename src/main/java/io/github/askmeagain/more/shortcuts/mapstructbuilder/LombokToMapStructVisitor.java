package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.CollectedData;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.InputObjectContainer;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.SourceContainer;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.printer.MapStructAnnotation;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.printer.MapStructMethod;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.printer.MapStructOverrideMethod;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LombokToMapStructVisitor extends JavaRecursiveElementVisitor {

  private final String packageName;
  private final Project project;
  private PsiType outputType;
  private final List<PsiReferenceExpressionImpl> stack = new ArrayList<>();
  private final Map<PsiType, Map<MapStructAnnotation, MapStructAnnotation>> mappings = new HashMap<>();

  public CollectedData collectResults() {

    var inputObjects = mappings.values()
        .stream()
        .map(Map::values)
        .flatMap(Collection::stream)
        .filter(x -> x.getSource() != null)
        .map(MapStructAnnotation::getInputObjects)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());

    var overrideMethods = mappings.values()
        .stream()
        .map(Map::values)
        .flatMap(Collection::stream)
        .filter(Objects::nonNull)
        .filter(x -> x.getSource() != null)
        .filter(x -> x.getSource().isExternalMethod())
        .map(x -> MapStructOverrideMethod.builder()
            .targets(x.getTargets())
            .originalList(x.getSource().getOriginalList())
            .build())
        .collect(Collectors.toList());

    var mapStructMethodList = transformToMapstructMethodList();

    return CollectedData.builder()
        .overrideMethods(overrideMethods)
        .mapStructMethodList(mapStructMethodList)
        .packageName(packageName)
        .inputObjects(inputObjects)
        .outputType(outputType)
        .build();
  }

  @Override
  public void visitExpressionList(PsiExpressionList list) {
    var parentName = ((PsiReferenceExpressionImpl) list.getParent().getChildren()[0]);
    var expression = list.getText();
    var hasExpression = expression.length() > 2;

    if (hasExpression) {
      if (expression.contains(".builder()")) {
        stack.add(parentName);
      } else {

        var inputObjects = getInputObjects(list);

        String constant;
        SourceContainer source;
        if (list.getChildren()[1] instanceof PsiLiteralExpression) {
          source = null;
          constant = ", constant=\"" + StringUtils.strip(list.getChildren()[1].getText(), "\"") + "\"";
        } else {
          source = getSource(list);
          constant = "";
        }

        var mapping = MapStructAnnotation.builder()
            .targets(stack)
            .target(parentName)
            .constant(constant)
            .inputObjects(new ArrayList<>(inputObjects))
            .source(source)
            .build();

        var psiType = LombokToMapstructUtils.resolveBuilder(parentName.getType(), project);
        var type = stack.isEmpty() ? outputType : psiType;

        mappings.computeIfAbsent(type, x -> new HashMap<>());
        mappings.get(type).put(mapping, mapping);

        //we also need to add the submapping
        if (!stack.isEmpty()) {
          var resolveBuilder = LombokToMapstructUtils.resolveBuilder(stack.get(stack.size() - 1).getType(), project);

          mappings.computeIfAbsent(resolveBuilder, x -> new HashMap<>());
          var result = mappings.get(resolveBuilder);

          var key = MapStructAnnotation.builder().targets(stack).build();
          if (!result.containsKey(key)) {
            mappings.get(resolveBuilder).put(key, MapStructAnnotation.builder()
                .targets(stack)
                .constant("")
                .inputObjects(inputObjects)
                .source(SourceContainer.builder()
                    .nestedMethodCall(true)
                    .nestedMethodType(psiType)
                    .originalList(list)
                    .build())
                .build());
          } else {
            var newMapping = result.get(key).toBuilder()
                .inputObjects(inputObjects)
                .build();
            result.put(newMapping, newMapping);
          }
        }
      }
    } else {
      if ("build".equals(parentName.getReferenceName())) {
        if (!stack.isEmpty()) {
          stack.remove(stack.size() - 1);
        } else {
          outputType = ((PsiMethodCallExpressionImpl) list.getParent()).getType();
        }
      }
    }

    super.visitExpressionList(list);
  }

  private SourceContainer getSource(PsiExpressionList list) {
    var result = new ArrayList<String>();

    list.accept(new JavaRecursiveElementVisitor() {
      @Override
      public void visitIdentifier(PsiIdentifier identifier) {
        super.visitIdentifier(identifier);
        result.add(identifier.getText());
      }
    });

    var isExternalMethod = list.getText().contains("+");

    var sourceString = result.stream()
        .map(this::removeGetter)
        .collect(Collectors.joining("."));

    return SourceContainer.builder()
        .originalList(list)
        .sourceString(sourceString)
        .externalMethod(isExternalMethod)
        .build();
  }

  private String removeGetter(String identifier) {
    try {
      if (identifier.startsWith("get") && Character.isUpperCase(identifier.charAt(3))) {
        return makeLowerCase(identifier.replace("get", ""));
      }
      return identifier;
    } catch (Exception ignored) {
      return identifier;
    }
  }


  public static HashSet<InputObjectContainer> getInputObjects(PsiExpressionList list) {
    var inputObjects = new HashSet<InputObjectContainer>();
    list.accept(new JavaRecursiveElementVisitor() {
      @Override
      public void visitReferenceExpression(PsiReferenceExpression expression) {
        super.visitReferenceExpression(expression);
        if (!expression.getText().contains(".")) {

          var obj = InputObjectContainer.builder()
              .type(expression.getType())
              .varName(expression.getReferenceName())
              .build();

          inputObjects.add(obj);
        }
      }
    });

    return inputObjects;
  }

  private String makeLowerCase(String input) {
    return String.valueOf(input.charAt(0)).toLowerCase() + input.substring(1);
  }

  private Set<MapStructMethod> transformToMapstructMethodList() {
    var result = new HashMap<MapStructMethod, MapStructMethod>();
    for (var kv : mappings.entrySet()) {

      var inputs = kv.getValue().values()
          .stream()
          .map(MapStructAnnotation::getInputObjects)
          .flatMap(Collection::stream)
          .collect(Collectors.toSet());

      var output = kv.getKey() == null ? outputType : kv.getKey();

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

    return new HashSet<>(result.values());
  }
}
