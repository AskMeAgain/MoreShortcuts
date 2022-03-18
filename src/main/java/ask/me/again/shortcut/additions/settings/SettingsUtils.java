package ask.me.again.shortcut.additions.settings;

import com.intellij.ide.util.PropertiesComponent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.beans.PropertyChangeEvent;
import java.util.Objects;

public class SettingsUtils {

  private static String computeName(String name) {
    return "shortcut_additions_10" + name;
  }

  public static boolean getBoolean(String schemeName, Boolean defaultValue, PropertiesComponent instance) {
    return Boolean.parseBoolean(instance.getValue(computeName(schemeName), defaultValue.toString()));
  }

  public static String getString(String schemeName, String defaultValue, PropertiesComponent instance) {
    return instance.getValue(computeName(schemeName), defaultValue);
  }

  public static void setBoolean(String schemeName, Boolean value, PropertiesComponent instance) {
    instance.setValue(computeName(schemeName), value.toString());
  }

  public static void setString(String schemeName, String value, PropertiesComponent instance) {
    instance.setValue(computeName(schemeName), value);
  }

  //https://stackoverflow.com/a/27190162/5563263
  public static void addChangeListener(JTextComponent text, ChangeListener changeListener) {
    Objects.requireNonNull(text);
    Objects.requireNonNull(changeListener);
    DocumentListener dl = new DocumentListener() {
      private int lastChange = 0, lastNotifiedChange = 0;

      @Override
      public void insertUpdate(DocumentEvent e) {
        changedUpdate(e);
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        changedUpdate(e);
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        lastChange++;
        SwingUtilities.invokeLater(() -> {
          if (lastNotifiedChange != lastChange) {
            lastNotifiedChange = lastChange;
            changeListener.stateChanged(new ChangeEvent(text));
          }
        });
      }
    };
    text.addPropertyChangeListener("document", (PropertyChangeEvent e) -> {
      Document d1 = (Document) e.getOldValue();
      Document d2 = (Document) e.getNewValue();
      if (d1 != null) d1.removeDocumentListener(dl);
      if (d2 != null) d2.addDocumentListener(dl);
      dl.changedUpdate(null);
    });
    Document d = text.getDocument();
    if (d != null) d.addDocumentListener(dl);
  }

}
