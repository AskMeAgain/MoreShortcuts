package ask.me.again.shortcut.additions.introducemock.entities;

import ask.me.again.shortcut.additions.introducemock.exceptions.ClassFromTypeNotFoundException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PsiHelpers {

  public static String decapitalizeString(String string) {
    return string == null || string.isEmpty() ? "" : Character.toLowerCase(string.charAt(0)) + string.substring(1);
  }

  public static void print(Project project, String message) {
    Messages.showMessageDialog(project, message, "Error x(", null);
  }

  public static PsiClass getClassFromString(Project project, String name) throws ClassFromTypeNotFoundException {

    //substring until generic -> (char)60 == '<'
    int beginIndex = name.indexOf(60);

    if (beginIndex > -1) {
      name = name.substring(0, beginIndex);
    }

    var result = JavaPsiFacade.getInstance(project)
        .findClass(name, GlobalSearchScope.allScope(project));

    if (result == null) {
      throw new ClassFromTypeNotFoundException("FOUND Nothing");
    }
    return result;
  }
}
