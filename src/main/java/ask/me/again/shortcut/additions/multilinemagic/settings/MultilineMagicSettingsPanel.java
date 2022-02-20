package ask.me.again.shortcut.additions.multilinemagic.settings;

import ask.me.again.shortcut.additions.multilinemagic.MultilineUtils;
import ask.me.again.shortcut.additions.settings.SettingsCreator;
import com.intellij.ide.util.PropertiesComponent;

import javax.swing.*;
import java.awt.*;

import static ask.me.again.shortcut.additions.settings.SettingsUtils.addChangeListener;

public class MultilineMagicSettingsPanel extends SettingsCreator {

  public static final String MULTILINE_MAGIC_DEFAULT_VALUE = "multiline_magic_default_value";
  public static final String MULTILINE_MAGIC_DEFAULT_INTERVAL = "multiline_magic_default_interval";

  private JTextField defaultIntervalField;
  private JTextField defaultValueField;

  private String startValue;
  private String intervalValue;

  public MultilineMagicSettingsPanel(PropertiesComponent propertiesComponent) {
    super(propertiesComponent);
  }

  public String getName() {
    return "Multiline Magic";
  }

  public JComponent getSettingsWindow() {
    JPanel panel = new JPanel(new SpringLayout());

    var defaultValueLabel = new JLabel("Default Start Value", JLabel.TRAILING);
    var defaultIntervalLabel = new JLabel("Default Interval Value", JLabel.TRAILING);

    defaultValueField = new JTextField(getString(MULTILINE_MAGIC_DEFAULT_VALUE, "0"), 10);
    defaultIntervalField = new JTextField(getString(MULTILINE_MAGIC_DEFAULT_INTERVAL, "1"), 10);

    defaultValueLabel.setLabelFor(defaultValueField);
    defaultValueLabel.setLabelFor(defaultIntervalField);

    panel.add(defaultValueLabel);
    panel.add(defaultValueField);
    panel.add(defaultIntervalLabel);
    panel.add(defaultIntervalField);

    startValue = defaultValueField.getText();
    intervalValue = defaultIntervalField.getText();

    addChangeListener(defaultValueField, e -> startValue = defaultValueField.getText());
    addChangeListener(defaultIntervalField, e -> intervalValue = defaultIntervalField.getText());

    MultilineUtils.makeCompactGrid(
        panel,
        2, 2, //rows, cols
        6, 6,        //initX, initY
        6, 6);       //xPad, yPad

    var outerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    outerPanel.add(panel);
    return outerPanel;
  }

  @Override
  public void save() {
    setString(MULTILINE_MAGIC_DEFAULT_VALUE, startValue);
    setString(MULTILINE_MAGIC_DEFAULT_INTERVAL, intervalValue);
  }

}
