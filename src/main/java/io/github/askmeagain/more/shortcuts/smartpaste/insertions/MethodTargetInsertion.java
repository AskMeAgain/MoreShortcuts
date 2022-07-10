package io.github.askmeagain.more.shortcuts.smartpaste.insertions;

import io.github.askmeagain.more.shortcuts.smartpaste.SmartInsertionUtils;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class MethodTargetInsertion implements SmartInsertion {

  @Override
  public boolean isApplicable(String originalText, String clipboard, AnActionEvent e) {
    var normalBrackets = originalText.contains("(") && originalText.contains(")");
    var swiftyBrackets = originalText.contains("{") && originalText.contains("}");
    return normalBrackets || swiftyBrackets;
  }

  @Override
  public String apply(String originalText, String clipboard, AnActionEvent e) {

    var startMarker = originalText.contains("(") ? "(" : "{";
    var endMarker = originalText.contains(")") ? ")" : "}";

    var textBeforeBracket = originalText.substring(0, originalText.indexOf(startMarker) + 1);
    var textAfterBracket = originalText.substring(originalText.indexOf(endMarker));

    var newText = textBeforeBracket + clipboard + textAfterBracket;
    return SmartInsertionUtils.normalizeSemicolon(newText);
  }
}
