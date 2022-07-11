package io.github.askmeagain.more.shortcuts.multilinemagic;

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
      JPanel panel = new JPanel(new SpringLayout());

      var startLabel = new JLabel("Start", JLabel.TRAILING);
      var intervalLabel = new JLabel("Interval", JLabel.TRAILING);

      startField = new JTextField("" + state.getMMStartValue(), 10);
      endField = new JTextField("" + state.getMMIntervalValue(), 10);

      startLabel.setLabelFor(startField);
      startLabel.setLabelFor(endField);

      panel.add(startLabel);
      panel.add(startField);

      panel.add(intervalLabel);
      panel.add(endField);

      MultilineUtils.makeCompactGrid(panel,
          2, 2, //rows, cols
          6, 6,        //initX, initY
          6, 6);       //xPad, yPad

      return panel;
    }
  }
}