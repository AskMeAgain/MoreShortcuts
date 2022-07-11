package io.github.askmeagain.more.shortcuts.multilinemagic;

import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import io.github.askmeagain.more.shortcuts.settings.MoreShortcutState;
import io.github.askmeagain.more.shortcuts.settings.PersistenceManagementService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogWrapper;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@NoArgsConstructor
public class MultilineCountAction extends AnAction {


  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    new ConnectServerDialog(e).show();
  }


  public static class ConnectServerDialog extends DialogWrapper {

    private final MoreShortcutState state = PersistenceManagementService.getInstance().getState();
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

      startField = new JTextField("" + state.getMMStartValue(), 10);
      endField = new JTextField("" + state.getMMIntervalValue(), 10);

      return FormBuilder.createFormBuilder()
          .addLabeledComponent(new JBLabel("Start",JLabel.TRAILING), startField, 1, false)
          .addLabeledComponent(new JBLabel("Interval",JLabel.TRAILING), endField, 1, false)
          .addComponentFillVertically(new JPanel(), 0)
          .getPanel();
    }
  }
}