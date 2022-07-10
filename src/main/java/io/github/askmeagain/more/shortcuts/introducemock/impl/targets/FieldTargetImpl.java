package io.github.askmeagain.more.shortcuts.introducemock.impl.targets;

import io.github.askmeagain.more.shortcuts.settings.MoreShortcutState;
import io.github.askmeagain.more.shortcuts.settings.PersistenceManagementService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiParameter;
import io.github.askmeagain.more.shortcuts.commons.PsiHelpers;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FieldTargetImpl extends TargetBase {

  private final MoreShortcutState state = PersistenceManagementService.getInstance().getState();

  public FieldTargetImpl(PsiFile psiFile, Project project) {
    super(psiFile, project);
  }

  public PsiElement createExpression(PsiParameter psiParameter) {
    var presentableText = psiParameter.getType().getPresentableText();

    var variableName = PsiHelpers.decapitalizeString(presentableText);

    if (variableName.contains("<")) {
      variableName = variableName.substring(0, variableName.indexOf("<"));
    }

    PsiField fieldFromText = factory.createFieldFromText(presentableText + " " + variableName + ";", null);
    fieldFromText.getModifierList().addAnnotation("Mock");

    if (state.getIntroduceMockFieldPrivateField()) {
      fieldFromText.getModifierList().add(factory.createKeyword("private"));
    }

    return fieldFromText;
  }

  @Override
  public List<String> extractVariableNames(List<PsiElement> result) {
    var shortDict = new HashMap<String, Integer>();

    //first run to check which variables are duplicate:
    var duplicateMap = result.stream()
        .map(x -> (PsiField) x)
        .filter(Objects::nonNull)
        .map(PsiField::getName)
        .collect(Collectors.toMap(Function.identity(), x -> 1, Integer::sum));

    return result.stream()
        .map(x -> (PsiField) x)
        .map(x -> {
          if (x == null) {
            return null;
          }
          var name = x.getName();
          if (duplicateMap.get(name) > 1) {
            var index = shortDict.getOrDefault(name, 1);
            x.setName(name + index);
            shortDict.put(name, index + 1);
            return name + index;
          } else {
            return name;
          }
        })
        .collect(Collectors.toList());
  }

  @Override
  public void writeExpressionsToCode(PsiElement realAnchor, List<PsiElement> mockExpressions, List<Boolean> changeMap) {
    for (int i = mockExpressions.size() - 1; i >= 0; i--) {
      if (changeMap.get(i)) {
        realAnchor.addAfter(mockExpressions.get(i), null);
      }
    }
  }
}
