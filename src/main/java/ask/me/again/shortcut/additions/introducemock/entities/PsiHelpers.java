package ask.me.again.shortcut.additions.introducemock.entities;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLocalVariable;
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
}
