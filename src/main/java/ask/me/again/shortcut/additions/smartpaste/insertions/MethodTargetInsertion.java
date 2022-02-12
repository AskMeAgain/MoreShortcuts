package ask.me.again.shortcut.additions.smartpaste.insertions;

import ask.me.again.shortcut.additions.smartpaste.SmartInsertionUtils;

public class MethodTargetInsertion implements SmartInsertion {

  @Override
  public boolean isApplicable(String originalText, String clipboard) {
    var normalBrackets = originalText.contains("(") && originalText.contains(")");
    var swiftyBrackets = originalText.contains("{") && originalText.contains("}");
    return normalBrackets || swiftyBrackets;
  }

  @Override
  public String apply(String originalText, String clipboard) {

    var startMarker = originalText.contains("(") ? "(" : "{";
    var endMarker = originalText.contains(")") ? ")" : "}";

    var textBeforeBracket = originalText.substring(0, originalText.indexOf(startMarker) + 1);
    var textAfterBracket = originalText.substring(originalText.indexOf(endMarker));

    return SmartInsertionUtils.normalizeSemicolon(textBeforeBracket + clipboard + textAfterBracket);
  }
}
