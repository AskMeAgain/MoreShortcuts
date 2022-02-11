package ask.me.again.shortcut.additions.naming;

import ask.me.again.shortcut.additions.naming.entities.*;

import java.util.List;

public class NamingSchemeUtils {

  private static final List<NamingScheme> SCHEMES = List.of(
      new SnakeCaseImpl(),
      new DoenerCaseImpl(),
      new CamelCaseImpl(),
      new PascalCaseImpl(),
      new DotCaseImpl()
  );

  public static String applyNext(String text, int i) {
    var index = (i + 1) % SCHEMES.size();
    return SCHEMES.get(index).apply(text);
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

  public static String makeFirstLetterUppercase(String text){
    var firstChar = String.valueOf(text.charAt(0)).toUpperCase();
    return firstChar + text.substring(1);
  }

  public static String makeFirstLetterLowercase(String text){
    var firstChar = String.valueOf(text.charAt(0)).toLowerCase();
    return firstChar + text.substring(1);
  }
}
