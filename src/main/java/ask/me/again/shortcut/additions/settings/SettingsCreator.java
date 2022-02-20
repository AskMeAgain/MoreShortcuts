package ask.me.again.shortcut.additions.settings;

import com.intellij.ide.util.PropertiesComponent;

import javax.swing.*;

import static ask.me.again.shortcut.additions.settings.SettingsUtils.computeName;

public abstract class SettingsCreator {

  private final PropertiesComponent instance;

  public SettingsCreator(PropertiesComponent propertiesComponent) {
    this.instance = propertiesComponent;
  }

  public abstract String getName();

  public abstract JComponent getSettingsWindow();

  public abstract void save();

  protected boolean getBoolean(String schemeName) {
    return instance.getBoolean(computeName(schemeName), false);
  }

  protected boolean getBoolean(String schemeName, Boolean defaultValue) {
    return instance.getBoolean(computeName(schemeName), defaultValue);
  }

  protected String getString(String schemeName, String defaultValue) {
    return getString(instance, computeName(schemeName), defaultValue);
  }

  protected void setString(String schemeName, Boolean value) {
    instance.setValue(computeName(schemeName), value);
  }

  protected void setString(String schemeName, String value) {
    instance.setValue(computeName(schemeName), value);
  }

  public static String getString(PropertiesComponent instance, String schemeName, String defaultValue) {
    return instance.getValue(computeName(schemeName), defaultValue);
  }
}
