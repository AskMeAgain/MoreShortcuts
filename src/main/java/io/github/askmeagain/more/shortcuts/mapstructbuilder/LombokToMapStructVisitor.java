package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LombokToMapStructVisitor extends JavaRecursiveElementVisitor {

  private final String packageName;
  private PsiType outputType;
  private final List<String> stack = new ArrayList<>();
  private final List<Mapping> mappings = new ArrayList<>();

  public MappingContainer getResult() {

    var inputObjects = mappings.stream()
        .filter(x -> x.getSource() != null)
        .filter(x -> !x.getSource().isExternalMethod())
        .map(Mapping::getInputObjects)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());

    var overrideMethods = mappings.stream()
        .filter(Objects::nonNull)
        .filter(x -> x.getSource() != null)
        .filter(x -> x.getSource().isExternalMethod())
        .collect(Collectors.toList());

    return MappingContainer.builder()
        .overrideMethods(overrideMethods)
        .mappings(mappings)
        .packageName(packageName)
        .inputObjects(new ArrayList<>(inputObjects))
        .outputType(outputType)
        .build();
  }

  @Override
  public void visitExpressionList(PsiExpressionList list) {
    var parentName = ((PsiReferenceExpressionImpl) list.getParent().getChildren()[0]).getReferenceName();
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

        var mapping = Mapping.builder()
            .targets(stack)
            .target(parentName)
            .constant(constant)
            .inputObjects(new ArrayList<>(inputObjects))
            .source(source)
            .build();

        mappings.add(mapping);
      }
    } else {
      if ("build".equals(parentName)) {
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
    if (identifier.startsWith("get") && Character.isUpperCase(identifier.charAt(3))) {
      return makeLowerCase(identifier.replace("get", ""));
    }

    return identifier;
  }


  private HashSet<InputObjectContainer> getInputObjects(PsiExpressionList list) {
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
}
