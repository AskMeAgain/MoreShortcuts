package ask.me.again.shortcut.additions.smartpaste.insertions;

import ask.me.again.shortcut.additions.smartpaste.SmartInsertionUtils;

import java.util.Map;

public class BracketsAtEndInsertion implements SmartInsertion {

  private final Map<Character, Character> bracketMap = Map.of(
      '{', '}',
      '(', ')'
  );

  @Override
  public boolean isApplicable(String originalText, String clipboardText) {
    return clipboardText.endsWith("{") || clipboardText.endsWith("(");
  }

  @Override
  public String apply(String originalText, String clipboard) {
    var bracketEnd = clipboard.charAt(clipboard.length() - 1);

    if (bracketEnd == '(') {
      return SmartInsertionUtils.normalizeSemicolon(clipboard + originalText + bracketMap.get(bracketEnd));
    }

    return clipboard + "\n" + originalText + "\n" + bracketMap.get(bracketEnd);
  }
}
