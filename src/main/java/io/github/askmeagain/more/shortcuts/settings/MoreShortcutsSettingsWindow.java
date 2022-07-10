package io.github.askmeagain.more.shortcuts.settings;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;

public class MoreShortcutsSettingsWindow {

  private final JPanel settingsPanel;

  private final JBTextField multilineMagicStartValue = new JBTextField();
  private final JBTextField multilineMagicIntervalValue = new JBTextField();
  private final JBCheckBox staticImports = new JBCheckBox("Static imports");
  private final JBCheckBox pascalCaseImpl = new JBCheckBox("Pascal case");
  private final JBCheckBox camelCaseImpl = new JBCheckBox("Camel case");
  private final JBCheckBox dotCaseImpl = new JBCheckBox("Dot case");
  private final JBCheckBox doenerCaseImpl = new JBCheckBox("Doener case");
  private final JBCheckBox snakeCaseImpl = new JBCheckBox("Snake case");
  private final JBCheckBox introduceMockFieldPrivateField = new JBCheckBox("Introduce fieldMock private field");

  public MoreShortcutsSettingsWindow() {
    settingsPanel = FormBuilder.createFormBuilder()
        .addLabeledComponent(new JBLabel("Default start value: "), multilineMagicStartValue, 1, false)
        .addLabeledComponent(new JBLabel("Default interval value: "), multilineMagicIntervalValue, 1, false)
        .addComponent(staticImports)
        .addComponent(introduceMockFieldPrivateField)
        .addComponent(new JSeparator())
        .addComponent(pascalCaseImpl)
        .addComponent(camelCaseImpl)
        .addComponent(dotCaseImpl)
        .addComponent(doenerCaseImpl)
        .addComponent(snakeCaseImpl)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();
  }

  public JPanel getPanel() {
    return settingsPanel;
  }

  public JComponent getPreferredFocusedComponent() {
    return multilineMagicStartValue;
  }

  public Integer getMultiLineMagicStartValue() {
    return Integer.parseInt(multilineMagicStartValue.getText());
  }

  public Integer getMultiLineMagicIntervalValue() {
    return Integer.parseInt(multilineMagicIntervalValue.getText());
  }

  public Boolean getpascalCaseImpl() {
    return pascalCaseImpl.isSelected();
  }

  public void setpascalCaseImpl(boolean selected) {
    pascalCaseImpl.setSelected(selected);
  }

  public Boolean getcamelCaseImpl() {
    return camelCaseImpl.isSelected();
  }

  public void setcamelCaseImpl(boolean selected) {
    camelCaseImpl.setSelected(selected);
  }

  public Boolean getdotCaseImpl() {
    return dotCaseImpl.isSelected();
  }

  public void setdotCaseImpl(boolean selected) {
    dotCaseImpl.setSelected(selected);
  }

  public Boolean getdoenerCaseImpl() {
    return doenerCaseImpl.isSelected();
  }

  public void setdoenerCaseImpl(boolean selected) {
    doenerCaseImpl.setSelected(selected);
  }

  public Boolean getsnakeCaseImpl() {
    return snakeCaseImpl.isSelected();
  }

  public void setsnakeCaseImpl(boolean selected) {
    snakeCaseImpl.setSelected(selected);
  }

  public Boolean getStaticImports() {
    return staticImports.isSelected();
  }

  public void setStaticImports(boolean selected) {
    staticImports.setSelected(selected);
  }

  public Boolean getIntroduceMockFieldPrivateField() {
    return introduceMockFieldPrivateField.isSelected();
  }

  public void setIntroduceMockFieldPrivateField(boolean selected) {
    introduceMockFieldPrivateField.setSelected(selected);
  }

  public void setMultiLineMagicStartValue(Integer newMmStartValue) {
    multilineMagicStartValue.setText(String.valueOf(newMmStartValue));
  }

  public void setMultiLineMagicIntervalValue(Integer newMmIntervalValue) {
    multilineMagicIntervalValue.setText(String.valueOf(newMmIntervalValue));
  }

}