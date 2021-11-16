package ask.me.again.shortcut.additions.introducemock.impl.targets;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
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
          if(duplicateMap.get(name) > 1){
            var index = shortDict.getOrDefault(name, 1);
            x.setName(name + index);
            shortDict.put(name, index + 1);
            return name + index;
          }else{
            return name;
          }
        })
        .collect(Collectors.toList());
  }

  @Override
  public void writeExpressionsToCode(PsiElement targetAnchor, List<PsiElement> mockExpressions, List<Boolean> changeMap) {
    var realAnchor = targetAnchor.getParent();

    for (int i = 0; i < mockExpressions.size(); i++) {
      if (changeMap.get(i)) {
        realAnchor.addBefore(mockExpressions.get(i), targetAnchor);
      }
    }
  }
}
