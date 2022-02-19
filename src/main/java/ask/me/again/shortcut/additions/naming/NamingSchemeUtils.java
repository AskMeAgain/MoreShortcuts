package ask.me.again.shortcut.additions.naming;

import ask.me.again.shortcut.additions.naming.entities.*;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class NamingSchemeUtils {

  public static final List<NamingScheme> SCHEMES = List.of(
      new SnakeCaseImpl(),
      new CamelCaseImpl(),
      new DoenerCaseImpl(),
      new PascalCaseImpl(),
      new DotCaseImpl()
  );

  public static String applyNext(String text, int startIndex, Project project) {
    var instance = PropertiesComponent.getInstance(project);

    return Stream.of(SCHEMES, SCHEMES)
        .flatMap(Collection::stream)
        .skip(startIndex % SCHEMES.size())
        .filter(x -> instance.getBoolean(NamingSchemeSettingsPanel.computeName(x.getName())))
        .map(x -> x.apply(text))
        .findFirst()
        .orElse(text);
  }

  public static Character findSeparator(String text) {
    if (text.contains("_")) {
      return '_';
    } else if (text.contains("-")) {
      return '-';
    } else if (text.contains(".")) {
      return '.';
    } else {
      return 'n';
    }
  }

  public static String makeUpperCaseNextToSeparator(String text, Character separator) {
    var result = new StringBuilder();
    char[] charArray = text.toCharArray();

    result.append(charArray[0]);
    for (int i = 1; i < charArray.length; i++) {
      if (charArray[i - 1] == separator) {
        result.append(Character.toUpperCase(charArray[i]));
      } else {
        result.append(charArray[i]);
      }
    }
    return result.toString();
  }

  public static String makeFirstLetterUppercase(String text) {
    var firstChar = String.valueOf(text.charAt(0)).toUpperCase();
    return firstChar + text.substring(1);
  }

  public static String makeFirstLetterLowercase(String text) {
    var firstChar = String.valueOf(text.charAt(0)).toLowerCase();
    return firstChar + text.substring(1);
  }
}
