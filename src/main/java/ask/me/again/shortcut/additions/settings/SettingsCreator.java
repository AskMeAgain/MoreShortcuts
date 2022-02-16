package ask.me.again.shortcut.additions.settings;

import javax.swing.*;

public interface SettingsCreator {

  String getName();

  JComponent getSettingsWindow();

  void save();
}
