package ask.me.again.shortcut.additions.introducemock;

import ask.me.again.shortcut.additions.multilinemagic.MultilineUtils;
import ask.me.again.shortcut.additions.settings.SettingsCreator;
import com.intellij.ide.util.PropertiesComponent;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class IntroduceMockSettingsPanel extends SettingsCreator {

  public static final String PRIVATE_FIELD = "private_field";

  private Boolean privateFields;

  public IntroduceMockSettingsPanel(PropertiesComponent propertiesComponent) {
    super(propertiesComponent);
    privateFields = getBoolean(PRIVATE_FIELD, false);
  }

  public String getName() {
    return "Introduce Mock/Field";
  }

  public JComponent getSettingsWindow() {
    var panel = new JPanel(new SpringLayout());
    var checkBox = new JCheckBox("Make Fields private");

    checkBox.setSelected(privateFields);
    checkBox.addItemListener(e -> privateFields = e.getStateChange() == ItemEvent.SELECTED);

    panel.add(checkBox);

    MultilineUtils.makeCompactGrid(
        panel,
        1, 1, //rows, cols
        6, 6,        //initX, initY
        6, 6);       //xPad, yPad

    return panel;
  }

  @Override
  public void save() {
    setString(PRIVATE_FIELD, privateFields);
  }
}
