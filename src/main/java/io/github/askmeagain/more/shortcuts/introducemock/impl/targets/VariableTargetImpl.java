package io.github.askmeagain.more.shortcuts.introducemock.impl.targets;

import com.intellij.psi.impl.PsiParserFacadeImpl;
import io.github.askmeagain.more.shortcuts.introducemock.entities.MockType;
import io.github.askmeagain.more.shortcuts.settings.MoreShortcutState;
import io.github.askmeagain.more.shortcuts.settings.PersistenceManagementService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import io.github.askmeagain.more.shortcuts.commons.PsiHelpers;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VariableTargetImpl extends TargetBase {

  private final MoreShortcutState state = PersistenceManagementService.getInstance().getState();

  public VariableTargetImpl(PsiFile psiFile, Project project, MockType mockType) {
    super(psiFile, project, mockType);
  }

  public PsiDeclarationStatement createExpression(PsiParameter psiParameter) {
    var presentableText = psiParameter.getType().getPresentableText();

    if (presentableText.contains("<")) {
      presentableText = presentableText.substring(0, presentableText.indexOf("<"));
    }

    var codeText = String.format("Mockito.%s(%s.class)", mockType.toString(), presentableText);
    if (state.getStaticImports()) {
      codeText = codeText.replaceFirst("Mockito\\.", "");
    }
    var equalsCall = (PsiMethodCallExpression) factory.createExpressionFromText(codeText, null);

    var varType = factory.createTypeByFQClassName("var");

    return factory.createVariableDeclarationStatement(PsiHelpers.decapitalizeString(presentableText), varType, equalsCall);
  }

  @Override
  public List<String> extractVariableNames(List<PsiElement> result) {
    var shortDict = new HashMap<String, Integer>();

    //first run to check which variables are duplicate:
    var duplicateMap = result.stream()
        .map(x -> PsiTreeUtil.getChildOfType(x, PsiLocalVariable.class))
        .filter(Objects::nonNull)
        .map(PsiLocalVariable::getName)
        .collect(Collectors.toMap(Function.identity(), x -> 1, Integer::sum));

    return result.stream()
        .map(x -> PsiTreeUtil.getChildOfType(x, PsiLocalVariable.class))
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
  public void writeExpressionsToCode(PsiElement targetAnchor, List<PsiElement> mockExpressions, List<Boolean> changeMap) {
    var whitespace = new PsiParserFacadeImpl(project).createWhiteSpaceFromText("\n\n");

    var realAnchor = targetAnchor.getParent();

    for (int i = 0; i < mockExpressions.size(); i++) {
      if (changeMap.get(i)) {
        realAnchor.addBefore(mockExpressions.get(i), targetAnchor);
      }
    }
    realAnchor.addBefore(whitespace, targetAnchor);
  }
}
