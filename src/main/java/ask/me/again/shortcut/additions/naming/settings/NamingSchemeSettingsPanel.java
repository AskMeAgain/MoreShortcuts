package ask.me.again.shortcut.additions.naming.settings;

import ask.me.again.shortcut.additions.multilinemagic.MultilineUtils;
import ask.me.again.shortcut.additions.settings.SettingsCreator;
import com.intellij.ide.util.PropertiesComponent;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.HashMap;
import java.util.Map;

import static ask.me.again.shortcut.additions.naming.service.NamingSchemeService.*;

public class NamingSchemeSettingsPanel implements SettingsCreator {

  private final PropertiesComponent instance;
  private final Map<String, Boolean> allowedNamingSchemes = new HashMap<>();

  public NamingSchemeSettingsPanel(PropertiesComponent propertiesComponent) {
    this.instance = propertiesComponent;

    SCHEMES.forEach(scheme -> allowedNamingSchemes.put(scheme.getName(), getBooleanFromPersistence(scheme.getName())));
  }

  @Override
  public void save() {
    allowedNamingSchemes.forEach((k, v) -> instance.setValue(computeName(k), v));
  }

  public static String computeName(String name) {
    return "shortcut_naming_scheme_" + name;
  }

  @Override
  public String getName() {
    return "Naming Schemes";
  }

  @Override
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

    MultilineUtils.makeCompactGrid(
        jPanel,
        allowedNamingSchemes.size(), 1,
        6, 6,
        6, 6
    );

    return jPanel;
  }

  private boolean getBooleanFromPersistence(String schemeName) {
    return instance.getBoolean(computeName(schemeName), false);
  }
}
