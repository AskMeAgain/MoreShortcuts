package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Visitor extends JavaRecursiveElementVisitor {

  private final String packageName;
  private final PsiFile psiFile;
  private String outputType;
  private final List<String> stack = new ArrayList<>();
  private final List<Mapping> mappings = new ArrayList<>();

  public MappingContainer getResult() {

    var inputObjects = mappings.stream()
        .map(x -> x.getSource())
        .map(x -> x.getText())
        .collect(Collectors.toList());

    return MappingContainer.builder()
        .mappings(mappings)
        .packageName(packageName)
        .inputObjects(inputObjects)
        .outputType(outputType)
        .build();
  }

  @Override
  public void visitLocalVariable(PsiLocalVariable variable) {
    visitVariable(variable);
    outputType = variable.getTypeElement().getText();
  }

  @Override
  public void visitExpressionList(PsiExpressionList list) {

    var parentName = ((PsiReferenceExpressionImpl) list.getParent().getChildren()[0]).getReferenceName();
    var expressionText = list.getText();

    if (expressionText.length() > 2) {
      if (expressionText.contains(".builder()")) {
        stack.add(parentName);
      } else {
        var mapping = Mapping.builder()
            .targets(stack)
            .target(parentName)
            .constant("")
            .source(list)
            .build();

        mappings.add(mapping);
      }
    } else {
      if (parentName.equals("build")) {
        if (!stack.isEmpty()) {
          stack.remove(stack.size() - 1);
        } else {
          int i = 0;
        }
      }
    }

    super.visitExpressionList(list);
  }
}
