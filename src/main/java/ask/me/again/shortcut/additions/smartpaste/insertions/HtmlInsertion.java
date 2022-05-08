package ask.me.again.shortcut.additions.smartpaste.insertions;

import ask.me.again.shortcut.additions.smartpaste.SmartInsertionUtils;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.Map;

public class HtmlInsertion implements SmartInsertion {

  @Override
  public boolean isApplicable(String originalText, String clipboardText, AnActionEvent e) {
    var trimmedText = clipboardText.trim();
    return trimmedText.endsWith(">") && trimmedText.startsWith("<");
  }

  @Override
  public String apply(String originalText, String clipboard, AnActionEvent e) {
    var trimmedClipboard = clipboard.trim();
    return trimmedClipboard + "\n" + originalText + "\n" + trimmedClipboard.charAt(0) + "/" + trimmedClipboard.substring(1);
  }
}
