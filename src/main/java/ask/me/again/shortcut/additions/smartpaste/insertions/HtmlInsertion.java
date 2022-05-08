package ask.me.again.shortcut.additions.smartpaste.insertions;

import ask.me.again.shortcut.additions.smartpaste.SmartInsertionUtils;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.Map;

public class HtmlInsertion implements SmartInsertion {

  @Override
  public boolean isApplicable(String originalText, String clipboardText, AnActionEvent e) {
    return clipboardText.endsWith(">") && clipboardText.startsWith("<");
  }

  @Override
  public String apply(String originalText, String clipboard, AnActionEvent e) {
    return clipboard + "\n" + originalText + "\n" + clipboard.charAt(0) + "/" + clipboard.substring(1);
  }
}
