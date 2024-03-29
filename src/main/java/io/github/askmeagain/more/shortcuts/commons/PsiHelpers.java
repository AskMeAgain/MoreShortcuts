package io.github.askmeagain.more.shortcuts.commons;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class PsiHelpers {

  public static void print(Project project, String message) {
    Messages.showMessageDialog(project, message, "Error x(", null);
  }

  public static PsiClass getClassFromString(Project project, String name) {

    //substring until generic -> (char)60 == '<'
    int beginIndex = name.indexOf(60);

    if (beginIndex > -1) {
      name = name.substring(0, beginIndex);
    }

    var result = JavaPsiFacade.getInstance(project)
        .findClass(name, GlobalSearchScope.allScope(project));

    if (result == null) {
      throw new RuntimeException("Could not find class");
    }
    return result;
  }

  @SneakyThrows
  public static List<PsiIdentifier> findAllRecursivelyInBlock(PsiElement element) {

    for (int i = 0; i < 100; i++) {
      element = element.getParent();
      if (element instanceof PsiMethod) {
        break;
      }
    }

    var children = PsiTreeUtil.findChildrenOfType(element, PsiIdentifier.class);

    var result = new ArrayList<PsiIdentifier>();

    for (var child : children) {
      if (child.getParent() instanceof PsiParameter) {
        result.add(child);
      }
      if (child.getParent() instanceof PsiLocalVariable) {
        result.add(child);
      }
    }

    //find fields
    var classParent = element.getParent();
    PsiTreeUtil.findChildrenOfType(classParent, PsiField.class).forEach(field -> {
      var identifier = PsiTreeUtil.getChildOfType(field, PsiIdentifier.class);
      result.add(identifier);
    });

    return result;
  }
}
