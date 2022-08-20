package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiCapturedWildcardType;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.InputObjectContainer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LombokToMapstructUtils {

  public static PsiType resolveBuilder(PsiType temp, Project project) {
    try {
      var type = resolveCapture(temp);
      var presentableText = type.getPresentableText();
      var otherText = type.getCanonicalText();

      if (presentableText.contains("<")) {
        presentableText = presentableText.substring(0, presentableText.indexOf('<'));
        otherText = otherText.substring(0, type.getCanonicalText().indexOf('<'));
      }

      var replacedName = otherText.replaceAll("." + presentableText + "$", "");
      return PsiType.getTypeByName(replacedName, project, GlobalSearchScope.allScope(project));
    } catch (Exception e) {
      throw new RuntimeException(String.format("Cannot resolve builder because %s cannot be found", temp), e);
    }
  }

  public static List<InputObjectContainer> getSpecificMappingInputs(PsiExpressionList mapStructAnnotation) {
    return new ArrayList<>(LombokToMapStructVisitor.getInputObjects(mapStructAnnotation));
  }

  public static PsiType resolveCapture(PsiType psiType) {
    if (psiType.getPresentableText().contains("capture of ?")) {
      return ((PsiCapturedWildcardType) psiType).getUpperBound();
    } else {
      return psiType;
    }
  }
}
