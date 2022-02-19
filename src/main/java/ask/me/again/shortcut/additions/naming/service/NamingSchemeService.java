package ask.me.again.shortcut.additions.naming.service;

import ask.me.again.shortcut.additions.naming.settings.NamingSchemeSettingsPanel;
import ask.me.again.shortcut.additions.naming.entities.*;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NamingSchemeService {

  private final List<NamingScheme> schemes;

  public static List<NamingScheme> SCHEMES = List.of(
      new PascalCaseImpl(),
      new CamelCaseImpl(),
      new DotCaseImpl(),
      new DoenerCaseImpl(),
      new SnakeCaseImpl()
  );

  public NamingSchemeService(Project project) {
    var instance = PropertiesComponent.getInstance(project);

    schemes = SCHEMES.stream()
        .filter(x -> instance.getBoolean(NamingSchemeSettingsPanel.computeName(x.getName())))
        .collect(Collectors.toList());
  }

  public String applyNext(String text, int index) {
    return Stream.of(schemes, schemes)
        .flatMap(Collection::stream)
        .skip(index % schemes.size())
        .map(x -> x.apply(text))
        .findFirst()
        .orElse(text);
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
