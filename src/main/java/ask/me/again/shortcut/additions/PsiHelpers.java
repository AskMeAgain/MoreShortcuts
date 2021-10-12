package ask.me.again.shortcut.additions;

import ask.me.again.shortcut.additions.introducemock.exceptions.ClassFromTypeNotFoundException;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.List;

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
