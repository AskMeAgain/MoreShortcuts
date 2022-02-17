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
//    allowedNamingSchemes.put("doener-case", getBooleanFromPersistance(computeName("doener-case")));
//    allowedNamingSchemes.put("dot.case", getBooleanFromPersistance(computeName("dot.case")));
//    allowedNamingSchemes.put("PascalCase", getBooleanFromPersistance(computeName("PascalCase")));
//    allowedNamingSchemes.put("camelCase", getBooleanFromPersistance(computeName("camelCase")));
  }

  private String computeName(String name) {
    return "shortcut_naming_scheme_" + name;
  }

  private boolean getBooleanFromPersistance(String path) {
    var result = instance.getBoolean(path, false);
    System.out.println("Persistance: " + path + " -> " + result);
    return result;
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
        System.out.println("Voted: '" + computeName(k) + "' -> " + v1);
        allowedNamingSchemes.put(k, v1);
      });

      System.out.println("On Start: '" + k + "' -> " + isEnabled);
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
      System.out.println("Saved: '" + computeName(k) + "' -> " + v);
      instance.setValue(computeName(k), v);
    });
  }
}
