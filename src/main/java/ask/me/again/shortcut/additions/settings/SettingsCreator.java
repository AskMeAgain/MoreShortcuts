package ask.me.again.shortcut.additions.settings;

import com.intellij.ide.util.PropertiesComponent;

import javax.swing.*;

public abstract class SettingsCreator {

  private final PropertiesComponent instance;

  public SettingsCreator(PropertiesComponent propertiesComponent) {
    this.instance = propertiesComponent;
  }

  public abstract String getName();

  public abstract JComponent getSettingsWindow();

  public abstract void save();

  protected boolean getBoolean(String schemeName) {
    return SettingsUtils.getBoolean((schemeName), instance);
  }

  protected boolean getBoolean(String schemeName, Boolean defaultValue) {
    return SettingsUtils.getBoolean((schemeName), defaultValue, instance);
  }

  protected String getString(String schemeName, String defaultValue) {
    return getString(instance, (schemeName), defaultValue);
  }

  protected void setBoolean(String schemeName, Boolean value) {
    SettingsUtils.setBoolean((schemeName), value, instance);
  }

  protected void setString(String schemeName, String value) {
    SettingsUtils.setString((schemeName), value, instance);
  }

  public static String getString(PropertiesComponent instance, String schemeName, String defaultValue) {
    return SettingsUtils.getString((schemeName), defaultValue, instance);
  }
}
