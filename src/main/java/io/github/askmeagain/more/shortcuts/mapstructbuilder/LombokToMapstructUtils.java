package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiPolyadicExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import com.intellij.psi.search.GlobalSearchScope;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.entities.InputObjectContainer;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.printer.MapStructAnnotation;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.printer.MapStructMethod;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class LombokToMapstructUtils {

  public static PsiType resolveBuilder(PsiType type, Project project) {
    var presentableText = type.getPresentableText();
    var replacedName = type.getCanonicalText().replaceAll("." + presentableText + "$", "");
    return PsiType.getTypeByName(replacedName, project, GlobalSearchScope.allScope(project));
  }

  public static List<InputObjectContainer> getSpecificMappingInputs(PsiExpressionList mapStructAnnotation) {
    return new ArrayList<>(LombokToMapStructVisitor.getInputObjects(mapStructAnnotation));
  }

}
