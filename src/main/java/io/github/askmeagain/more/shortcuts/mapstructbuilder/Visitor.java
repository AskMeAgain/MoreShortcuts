package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import com.intellij.psi.util.PsiTreeUtil;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Visitor extends JavaRecursiveElementVisitor {

  private final String packageName;
  private PsiType outputType;
  private final List<String> stack = new ArrayList<>();
  private final List<Mapping> mappings = new ArrayList<>();

  public MappingContainer getResult() {

    var inputObjects = mappings.stream()
        .map(Mapping::getInputObjects)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());

    return MappingContainer.builder()
        .mappings(mappings)
        .packageName(packageName)
        .inputObjects(new ArrayList<>(inputObjects))
        .outputType(outputType)
        .build();
  }

  @Override
  public void visitExpressionList(PsiExpressionList list) {

    var parentName = ((PsiReferenceExpressionImpl) list.getParent().getChildren()[0]).getReferenceName();
    var expressionText = list.getText();

    if (expressionText.length() > 2) {
      if (expressionText.contains(".builder()")) {
        stack.add(parentName);
      } else {

        var inputObjects = getInputObjects(list);

        var mapping = Mapping.builder()
            .targets(stack)
            .target(parentName)
            .constant("")
            .inputObjects(new ArrayList<>(inputObjects))
            .source(getSource(list))
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

  private String getSource(PsiExpressionList list) {
    var result = new ArrayList<String>();
    list.accept(new JavaRecursiveElementVisitor() {
      @Override
      public void visitIdentifier(PsiIdentifier identifier) {
        super.visitIdentifier(identifier);
        result.add(identifier.getText());
      }
    });

    return result.stream()
        .map(this::removeGetter)
        .collect(Collectors.joining("."));
  }

  private String removeGetter(String identifier) {
    if (identifier.startsWith("get") && Character.isUpperCase(identifier.charAt(3))) {
      return LombokToMapStructUtils.makeLowerCase(identifier.replace("get", ""));
    }

    return identifier;
  }

  @NotNull
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
}
