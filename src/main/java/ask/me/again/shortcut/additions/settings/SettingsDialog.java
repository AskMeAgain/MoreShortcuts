package ask.me.again.shortcut.additions.settings;

import ask.me.again.shortcut.additions.introducemock.IntroduceMockSettingsPanel;
import ask.me.again.shortcut.additions.mockswitchtype.settings.SwitchMockVariantSettingsPanel;
import ask.me.again.shortcut.additions.multilinemagic.settings.MultilineMagicSettingsPanel;
import ask.me.again.shortcut.additions.naming.settings.NamingSchemeSettingsPanel;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBTabbedPane;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SettingsDialog extends DialogWrapper {

  private final List<SettingsCreator> settings;

  public SettingsDialog(AnActionEvent e) {
    super(true);

    this.setSize(1000, 500);

    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (int) ((dimension.getWidth() - 1000) / 2);
    int y = (int) ((dimension.getHeight() - 500) / 2);
    this.setLocation(x, y);

    var instance = PropertiesComponent.getInstance(e.getProject());

    this.settings = List.of(
        new IntroduceMockSettingsPanel(instance),
        new MultilineMagicSettingsPanel(instance),
        new SwitchMockVariantSettingsPanel(instance),
        new NamingSchemeSettingsPanel(instance)
    );

    setTitle("Settings");
    init();
  }

  @Override
  public void doOKAction() {
    settings.forEach(SettingsCreator::save);
    this.close(0);
  }

  @Override
  protected @Nullable JComponent createCenterPanel() {
    var tabbedPane = new JBTabbedPane();

    tabbedPane.setSize(1000, 500);

    for (var creator : settings) {
      tabbedPane.addTab(creator.getName(), null, creator.getSettingsWindow());
    }

    tabbedPane.setTabPlacement(JTabbedPane.LEFT);

    return tabbedPane;
  }
}