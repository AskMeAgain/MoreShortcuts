package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.InputObjectContainer;

import java.util.ArrayList;
import java.util.List;

public class LombokToMapstructUtils {

  public static PsiType resolveBuilder(PsiType type, Project project) {
    try {
      var presentableText = type.getPresentableText();
      var replacedName = type.getCanonicalText().replaceAll("." + presentableText + "$", "");
      return PsiType.getTypeByName(replacedName, project, GlobalSearchScope.allScope(project));
    } catch (Exception e) {
      throw new RuntimeException(String.format("Cannot resolve builder because %s cannot be found", type), e);
    }
  }

  public static List<InputObjectContainer> getSpecificMappingInputs(PsiExpressionList mapStructAnnotation) {
    return new ArrayList<>(LombokToMapStructVisitor.getInputObjects(mapStructAnnotation));
  }

}
