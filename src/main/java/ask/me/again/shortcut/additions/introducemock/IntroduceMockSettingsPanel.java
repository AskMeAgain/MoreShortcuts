package ask.me.again.shortcut.additions.introducemock;

import ask.me.again.shortcut.additions.multilinemagic.MultilineUtils;
import ask.me.again.shortcut.additions.settings.SettingsCreator;
import com.intellij.ide.util.PropertiesComponent;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class IntroduceMockSettingsPanel extends SettingsCreator {

  public static final String PRIVATE_FIELD = "private_field";
  public static final String INTRODUCE_MOCK_STATIC_IMPORT = "introduce_mock_static_import";

  private Boolean privateFields;
  private Boolean staticImport;

  public IntroduceMockSettingsPanel(PropertiesComponent propertiesComponent) {
    super(propertiesComponent);
    privateFields = getBoolean(PRIVATE_FIELD, false);
    staticImport = getBoolean(INTRODUCE_MOCK_STATIC_IMPORT, false);
  }

  public String getName() {
    return "Introduce Mock/Field";
  }

  public JComponent getSettingsWindow() {
    var panel = new JPanel(new SpringLayout());
    var privateFieldCheckbox = new JCheckBox("Make Fields private");
    var staticImportCheckbox = new JCheckBox("Static Imports");

    privateFieldCheckbox.setSelected(privateFields);
    staticImportCheckbox.setSelected(staticImport);

    privateFieldCheckbox.addItemListener(e -> privateFields = e.getStateChange() == ItemEvent.SELECTED);
    staticImportCheckbox.addItemListener(e -> staticImport = e.getStateChange() == ItemEvent.SELECTED);

    panel.add(privateFieldCheckbox);
    panel.add(staticImportCheckbox);

    MultilineUtils.makeCompactGrid(
        panel,
        2, 1, //rows, cols
        6, 6,        //initX, initY
        6, 6);       //xPad, yPad

    return panel;
  }

  @Override
  public void save() {
    setBoolean(PRIVATE_FIELD, privateFields);
    setBoolean(INTRODUCE_MOCK_STATIC_IMPORT, staticImport);
  }
}
