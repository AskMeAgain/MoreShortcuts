package io.github.askmeagain.more.shortcuts.smartpaste.insertions;

import io.github.askmeagain.more.shortcuts.smartpaste.SmartInsertionUtils;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class MethodOriginInsertion implements SmartInsertion {

  @Override
  public boolean isApplicable(String originalText, String clipboard, AnActionEvent e) {
    var normalBrackets = clipboard.contains("(") && clipboard.contains(")");
    var swiftyBrackets = clipboard.contains("{") && clipboard.contains("}");

    return normalBrackets || swiftyBrackets;
  }

  @Override
  public String apply(String originalText, String clipboard, AnActionEvent e) {

    var startMarker = clipboard.contains("(") ? "(" : "{";
    var endMarker = clipboard.contains(")") ? ")" : "}";

    var textBeforeBracket = clipboard.substring(0, clipboard.indexOf(startMarker) + 1);
    var textAfterBracket = clipboard.substring(clipboard.indexOf(endMarker));

    var newText = textBeforeBracket + originalText + textAfterBracket;
    return SmartInsertionUtils.normalizeSemicolon(newText);
  }
}
