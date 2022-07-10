package io.github.askmeagain.more.shortcuts.smartpaste.insertions;

import io.github.askmeagain.more.shortcuts.smartpaste.SmartInsertionUtils;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.Map;

public class BracketsAtEndInsertion implements SmartInsertion {

  private final Map<Character, Character> bracketMap = Map.of(
      '{', '}',
      '(', ')'
  );

  @Override
  public boolean isApplicable(String originalText, String clipboardText, AnActionEvent e) {
    return clipboardText.endsWith("{") || clipboardText.endsWith("(");
  }

  @Override
  public String apply(String originalText, String clipboard, AnActionEvent e) {
    var bracketEnd = clipboard.charAt(clipboard.length() - 1);

    if (bracketEnd == '(') {
      var newText = clipboard + originalText + bracketMap.get(bracketEnd);
      return SmartInsertionUtils.normalizeSemicolon(newText);
    }

    return clipboard + "\n" + originalText + "\n" + bracketMap.get(bracketEnd);
  }
}
