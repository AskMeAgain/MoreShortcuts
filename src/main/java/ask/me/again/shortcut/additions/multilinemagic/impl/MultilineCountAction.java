package ask.me.again.shortcut.additions.multilinemagic.impl;

import ask.me.again.shortcut.additions.multilinemagic.MultilineUtils;
import ask.me.again.shortcut.additions.settings.SettingsCreator;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogWrapper;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static ask.me.again.shortcut.additions.multilinemagic.settings.MultilineMagicSettingsPanel.MULTILINE_MAGIC_DEFAULT_INTERVAL;
import static ask.me.again.shortcut.additions.multilinemagic.settings.MultilineMagicSettingsPanel.MULTILINE_MAGIC_DEFAULT_VALUE;

@NoArgsConstructor
public class MultilineCountAction extends AnAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    new ConnectServerDialog(e).show();
  }

  public static class ConnectServerDialog extends DialogWrapper {

    private JTextField startField;
    private JTextField endField;
    private final AnActionEvent e;

    public ConnectServerDialog(AnActionEvent e) {
      super(true);

      this.e = e;

      setTitle("Count Settings");
      init();
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
      return startField;
    }

    @Override
    public void doOKAction() {
      try {
        var from = Integer.parseInt(startField.getText());
        var index = Integer.parseInt(endField.getText());
        MultilineUtils.extracted(e, from, index);
      } catch (NumberFormatException ex) {
        var from = startField.getText();
        var index = Integer.parseInt(endField.getText());
        MultilineUtils.extractedChar(e, from.toCharArray()[0], index);
      }

      this.close(0);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
      JPanel p = new JPanel(new SpringLayout());
      var instance = PropertiesComponent.getInstance(e.getProject());

      var l = new JLabel("Start", JLabel.TRAILING);
      var l2 = new JLabel("Interval", JLabel.TRAILING);

      p.add(l);
      p.add(l2);

      startField = new JTextField(SettingsCreator.getString(instance, MULTILINE_MAGIC_DEFAULT_VALUE, "0"), 10);
      endField = new JTextField(SettingsCreator.getString(instance, MULTILINE_MAGIC_DEFAULT_INTERVAL, "0"), 10);

      l.setLabelFor(startField);
      l.setLabelFor(endField);

      p.add(startField);
      p.add(endField);

      MultilineUtils.makeCompactGrid(p,
          2, 2, //rows, cols
          6, 6,        //initX, initY
          6, 6);       //xPad, yPad

      return p;
    }
  }
}