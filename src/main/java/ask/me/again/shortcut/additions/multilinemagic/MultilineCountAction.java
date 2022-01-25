package ask.me.again.shortcut.additions.multilinemagic;

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
    var dialog = new ConnectServerDialog(e);

    dialog.show();
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

      var l = new JLabel("Start", JLabel.TRAILING);
      p.add(l);
      startField = new JTextField("0", 10);
      l.setLabelFor(startField);
      p.add(startField);

      var l2 = new JLabel("Interval", JLabel.TRAILING);
      p.add(l2);
      endField = new JTextField(10);
      l.setLabelFor(endField);
      p.add(endField);

      MultilineUtils.makeCompactGrid(p,
          2, 2, //rows, cols
          6, 6,        //initX, initY
          6, 6);       //xPad, yPad

      return p;
    }
  }
}