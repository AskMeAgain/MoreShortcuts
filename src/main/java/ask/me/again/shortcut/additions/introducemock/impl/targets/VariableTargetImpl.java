package ask.me.again.shortcut.additions.introducemock.impl.targets;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static ask.me.again.shortcut.additions.PsiHelpers.decapitalizeString;

public class VariableTargetImpl extends TargetBase {

  public VariableTargetImpl(PsiFile psiFile, Project project) {
    super(psiFile, project);
  }

  public PsiDeclarationStatement createExpression(PsiParameter psiParameter) {
    var presentableText = psiParameter.getType().getPresentableText();
    var equalsCall = (PsiMethodCallExpression) factory.createExpressionFromText(String.format("Mockito.mock(%s.class)", presentableText), null);

    var varType = factory.createTypeByFQClassName("var");
    return factory.createVariableDeclarationStatement(decapitalizeString(presentableText), varType, equalsCall);
  }

  public List<String> extractVariableNames(List<PsiElement> result) {
    var shortDict = new HashMap<String, Integer>();

    return result.stream()
        .map(x -> PsiTreeUtil.getChildOfType(x, PsiLocalVariable.class))
        .map(x -> {
          if (x == null) {
            return null;
          }
          if (!shortDict.containsKey(x.getName())) {
            shortDict.put(x.getName(), 1);
          } else {
            var integer = shortDict.get(x.getName());
            shortDict.put(x.getName(), integer + 1);
            x.setName(x.getName() + integer);
          }
          return x.getName();
        })
        .collect(Collectors.toList());
  }

  @Override
  public void writeExpressionsToCode(PsiElement targetAnchor, List<PsiElement> mockExpressions, List<Boolean> changeMap) {
    var realAnchor = targetAnchor.getParent();

    for (int i = mockExpressions.size() - 1; i >= 0; i--) {
      if (changeMap.get(i)) {
        realAnchor.addBefore(mockExpressions.get(i), targetAnchor);
      }
    }
  }

}
