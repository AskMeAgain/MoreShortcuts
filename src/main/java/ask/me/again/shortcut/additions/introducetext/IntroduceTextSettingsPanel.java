package ask.me.again.shortcut.additions.introducetext;

import ask.me.again.shortcut.additions.multilinemagic.MultilineUtils;
import ask.me.again.shortcut.additions.settings.SettingsCreator;
import com.intellij.ide.util.PropertiesComponent;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class IntroduceTextSettingsPanel extends SettingsCreator {

  public static final String MOCK_METHOD_VERIFY_STATIC_IMPORT = "mock_method_verify_static_import";

  private Boolean staticImport;

  public IntroduceTextSettingsPanel(PropertiesComponent propertiesComponent) {
    super(propertiesComponent);
    staticImport = getBoolean(MOCK_METHOD_VERIFY_STATIC_IMPORT, false);
  }

  public String getName() {
    return "Mock Method/Verify";
  }

  public JComponent getSettingsWindow() {
    var panel = new JPanel(new SpringLayout());
    var staticImportCheckbox = new JCheckBox("Static Imports");

    staticImportCheckbox.setSelected(staticImport);
    staticImportCheckbox.addItemListener(e -> staticImport = e.getStateChange() == ItemEvent.SELECTED);

    panel.add(staticImportCheckbox);

    MultilineUtils.makeCompactGrid(
        panel,
        1, 1, //rows, cols
        6, 6,        //initX, initY
        6, 6);       //xPad, yPad

    return panel;
  }

  @Override
  public void save() {
    setBoolean(MOCK_METHOD_VERIFY_STATIC_IMPORT, staticImport);
  }

}
