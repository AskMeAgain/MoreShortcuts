package ask.me.again.shortcut.additions.settings;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class OpenSettingsAction extends AnAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var dialog = new SettingsDialog(e);

    dialog.show();
  }
}
