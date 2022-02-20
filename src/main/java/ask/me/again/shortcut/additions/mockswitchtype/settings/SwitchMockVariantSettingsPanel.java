package ask.me.again.shortcut.additions.mockswitchtype.settings;

import ask.me.again.shortcut.additions.multilinemagic.MultilineUtils;
import ask.me.again.shortcut.additions.settings.SettingsCreator;
import com.intellij.ide.util.PropertiesComponent;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class SwitchMockVariantSettingsPanel extends SettingsCreator {

  public static final String IMPORT_FLAG = "static_import_flag";

  private boolean staticImportFlag;

  public SwitchMockVariantSettingsPanel(PropertiesComponent propertiesComponent) {
    super(propertiesComponent);
    staticImportFlag = getBoolean(IMPORT_FLAG, false);
  }

  @Override
  public void save() {
    setString(IMPORT_FLAG, staticImportFlag);
  }

  @Override
  public String getName() {
    return "Switch Mock Variant";
  }

  @Override
  public JComponent getSettingsWindow() {
    var jPanel = new JPanel(new SpringLayout());

    var checkBox = new JCheckBox("Static import");
    var isEnabled = staticImportFlag;

    checkBox.setSelected(isEnabled);
    checkBox.addItemListener(e -> staticImportFlag = e.getStateChange() == ItemEvent.SELECTED);

    jPanel.add(checkBox);

    MultilineUtils.makeCompactGrid(
        jPanel,
        1, 1,
        6, 6,
        6, 6
    );

    return jPanel;
  }
}
