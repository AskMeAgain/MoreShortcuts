package io.github.askmeagain.more.shortcuts.smartpaste.insertions;

import com.intellij.openapi.actionSystem.AnActionEvent;

public class HtmlInsertion implements SmartInsertion {

  @Override
  public boolean isApplicable(String originalText, String clipboardText, AnActionEvent e) {
    var trimmedText = clipboardText.trim();
    return trimmedText.endsWith(">") && trimmedText.startsWith("<");
  }

  @Override
  public String apply(String originalText, String clipboard, AnActionEvent e) {
    var trimmedClipboard = clipboard.trim();

    var space = trimmedClipboard.indexOf(" ");

    if(space == -1){
      space = trimmedClipboard.length() - 1;
    }

    return trimmedClipboard + "\n" + originalText + "\n" + trimmedClipboard.charAt(0) + "/" + trimmedClipboard.substring(1, space) + ">";
  }
}
