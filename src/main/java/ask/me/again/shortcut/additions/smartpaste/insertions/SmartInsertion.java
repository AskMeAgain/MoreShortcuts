package ask.me.again.shortcut.additions.smartpaste.insertions;

import com.intellij.openapi.actionSystem.AnActionEvent;

public interface SmartInsertion {

  boolean isApplicable(String originalText, String clipboard, AnActionEvent anActionEvent);

  String apply(String originalText, String clipboard, AnActionEvent e);

}
