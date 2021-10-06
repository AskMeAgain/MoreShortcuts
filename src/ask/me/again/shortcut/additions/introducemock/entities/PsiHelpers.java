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

  public static List<String> extractVariableNames(List<PsiElement> result) {
    var shortDict = new HashMap<String, Integer>();

    return result.stream()
        .map(x -> PsiTreeUtil.getChildOfType(x, PsiLocalVariable.class))
        .map(x -> {
          if (!shortDict.containsKey(x.getName())) {
            shortDict.put(x.getName(), 1);
          } else {
            var integer = shortDict.get(x.getName());
            shortDict.put(x.getName(), integer + 1);
            x.setName(x.getName() + integer);
          }
          return x;
        })
        .map(PsiLocalVariable::getName)
        .collect(Collectors.toList());
  }

  public static String decapitalizeString(String string) {
    return string == null || string.isEmpty() ? "" : Character.toLowerCase(string.charAt(0)) + string.substring(1);
  }

  public static void print(Project project, String message) {
    Messages.showMessageDialog(project, message, "Error x(", null);
  }
}
