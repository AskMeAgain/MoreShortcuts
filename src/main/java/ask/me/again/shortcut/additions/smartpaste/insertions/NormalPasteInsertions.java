package ask.me.again.shortcut.additions.smartpaste.insertions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.apache.velocity.anakia.AnakiaElement;

public class NormalPasteInsertions implements SmartInsertion {
  @Override
  public boolean isApplicable(String originalText, String text, AnActionEvent e) {
    return true;
  }

  @Override
  public String apply(String originalText, String clipboard, AnActionEvent e) {
    return clipboard;
  }
}
