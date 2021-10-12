package ask.me.again.shortcut.additions.introducemock.impl.targets;

import ask.me.again.shortcut.additions.introducemock.entities.ExecutionTarget;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static ask.me.again.shortcut.additions.PsiHelpers.decapitalizeString;

public class FieldTargetImpl extends TargetBase {

  public FieldTargetImpl(PsiFile psiFile, Project project) {
    super(psiFile, project);
  }

  public PsiElement createExpression(PsiParameter psiParameter) {
    var presentableText = psiParameter.getType().getPresentableText();

    PsiField fieldFromText = factory.createFieldFromText(presentableText + " " + decapitalizeString(presentableText) + ";", null);
    fieldFromText.getModifierList().addAnnotation("Mock");
    return fieldFromText;
  }

  @Override
  public List<String> extractVariableNames(List<PsiElement> result) {
    var shortDict = new HashMap<String, Integer>();

    return result.stream()
        .map(x -> (PsiField) x)
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
  public void writeExpressionsToCode(PsiElement realAnchor, List<PsiElement> expressionList, List<Boolean> changeMap) {
    WriteCommandAction.runWriteCommandAction(project, () -> {
      for (int i = expressionList.size() - 1; i >= 0; i--) {
        if (changeMap.get(i)) {
          realAnchor.addAfter(expressionList.get(i), null);
        }
      }

      new ReformatCodeProcessor(psiFile, false).run();
    });
  }
}
