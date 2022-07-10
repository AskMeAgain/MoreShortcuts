package io.github.askmeagain.more.shortcuts.smartpaste;

public class SmartInsertionUtils {

  public static String normalizeSemicolon(String text) {
    if (text.contains(";")) {
      var replacedText = text.replaceAll(";", "");

      return replacedText + ";";
    }
    return text;
  }
}
