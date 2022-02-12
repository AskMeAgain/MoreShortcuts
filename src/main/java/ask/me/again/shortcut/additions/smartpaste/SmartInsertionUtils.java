package ask.me.again.shortcut.additions.smartpaste;

public class SmartInsertionUtils {

  public static String normalizeSemicolon(String text) {
    return text.replaceAll(";", "") + ";";
  }
}
