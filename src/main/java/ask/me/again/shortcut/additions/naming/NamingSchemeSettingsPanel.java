package ask.me.again.shortcut.additions.naming;

import ask.me.again.shortcut.additions.multilinemagic.MultilineUtils;
import ask.me.again.shortcut.additions.settings.SettingsCreator;
import com.intellij.ide.util.PropertiesComponent;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.HashMap;
import java.util.Map;

public class NamingSchemeSettingsPanel implements SettingsCreator {

  private final PropertiesComponent instance;
  private final Map<String, Boolean> allowedNamingSchemes = new HashMap<>();

  public NamingSchemeSettingsPanel(PropertiesComponent propertiesComponent) {
    this.instance = propertiesComponent;

    allowedNamingSchemes.put("SNAKE_CASE", getBooleanFromPersistance(computeName("SNAKE_CASE")));
    allowedNamingSchemes.put("doener-case", getBooleanFromPersistance(computeName("doener-case")));
    allowedNamingSchemes.put("dot.case", getBooleanFromPersistance(computeName("dot.case")));
    allowedNamingSchemes.put("PascalCase", getBooleanFromPersistance(computeName("PascalCase")));
    allowedNamingSchemes.put("camelCase", getBooleanFromPersistance(computeName("camelCase")));
  }

  public static String computeName(String name) {
    return "shortcut_naming_scheme_" + name;
  }

  private boolean getBooleanFromPersistance(String path) {
    return instance.getBoolean(path, false);
  }

  public String getName() {
    return "Naming Schemes";
  }

  public JComponent getSettingsWindow() {
    var jPanel = new JPanel(new SpringLayout());

    allowedNamingSchemes.forEach((k, v) -> {
      var checkBox = new JCheckBox(k);
      var isEnabled = allowedNamingSchemes.get(k);

      checkBox.setSelected(isEnabled);
      checkBox.addItemListener(e -> {
        var v1 = e.getStateChange() == ItemEvent.SELECTED;
        allowedNamingSchemes.put(k, v1);
      });

      jPanel.add(checkBox);
    });

    MultilineUtils.makeCompactGrid(jPanel,
        allowedNamingSchemes.size(), 1, //rows, cols
        6, 6,        //initX, initY
        6, 6);       //xPad, yPad

    return jPanel;
  }

  @Override
  public void save() {
    allowedNamingSchemes.forEach((k, v) -> {
      instance.setValue(computeName(k), v);
    });
  }
}
