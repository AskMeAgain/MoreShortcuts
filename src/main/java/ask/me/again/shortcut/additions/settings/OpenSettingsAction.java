package ask.me.again.shortcut.additions.settings;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class OpenSettingsAction extends AnAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    var dialog = new SettingsDialog(e);

    dialog.show();
  }
}
