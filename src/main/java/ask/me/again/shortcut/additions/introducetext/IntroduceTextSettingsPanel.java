package ask.me.again.shortcut.additions.introducetext;

import ask.me.again.shortcut.additions.multilinemagic.MultilineUtils;
import ask.me.again.shortcut.additions.settings.SettingsCreator;
import com.intellij.ide.util.PropertiesComponent;

import javax.swing.*;

public class IntroduceTextSettingsPanel extends SettingsCreator {

  public IntroduceTextSettingsPanel(PropertiesComponent propertiesComponent) {
      super(propertiesComponent);
  }

  public String getName(){
    return "Naming Schemes";
  }

  public JComponent getSettingsWindow() {
    JPanel p = new JPanel(new SpringLayout());

    var l = new JLabel("asdasdasdasdas", JLabel.TRAILING);
    p.add(l);
    var startField = new JTextField("0", 10);
    l.setLabelFor(startField);
    p.add(startField);

    var l2 = new JLabel("Interval", JLabel.TRAILING);
    p.add(l2);
    var endField = new JTextField(10);
    l.setLabelFor(endField);
    p.add(endField);

    MultilineUtils.makeCompactGrid(p,
        2, 2, //rows, cols
        6, 6,        //initX, initY
        6, 6);       //xPad, yPad

    return p;
  }

  @Override
  public void save() {
    //var instance = PropertiesComponent.getInstance();
    //instance.setValue("");
  }

}
