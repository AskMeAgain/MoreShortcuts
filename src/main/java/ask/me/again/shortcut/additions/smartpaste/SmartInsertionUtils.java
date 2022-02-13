package ask.me.again.shortcut.additions.smartpaste;

public class SmartInsertionUtils {

  public static String normalizeSemicolon(String text, String clipboard) {
    if (text.contains(";")) {
      var replacedText = text.replaceAll(";", "");

      return replacedText + ";";
    }
    return text;
  }
}
