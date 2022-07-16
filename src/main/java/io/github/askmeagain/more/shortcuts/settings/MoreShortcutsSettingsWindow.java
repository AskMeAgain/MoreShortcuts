package io.github.askmeagain.more.shortcuts.settings;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import lombok.Getter;

import javax.swing.*;

public class MoreShortcutsSettingsWindow {

  @Getter
  private final JBTabbedPane tabbedPane;

  private final JBTextField multilineMagicStartValue = new JBTextField();
  private final JBTextField multilineMagicIntervalValue = new JBTextField();
  private final JBTextField codeLenseFontSizeField = new JBTextField();
  private final JBCheckBox staticImports = new JBCheckBox("Static imports");
  private final JBCheckBox pascalCaseCheckbox = new JBCheckBox("Pascal case");
  private final JBCheckBox camelCaseCheckbox = new JBCheckBox("Camel case");
  private final JBCheckBox dotCaseCheckbox = new JBCheckBox("Dot case");
  private final JBCheckBox doenerCaseCheckbox = new JBCheckBox("Doener case");
  private final JBCheckBox snakeCaseCheckbox = new JBCheckBox("Snake case");
  private final JBCheckBox introduceMockFieldPrivateField = new JBCheckBox("Private field when introducing field");

  public MoreShortcutsSettingsWindow() {

    tabbedPane = new JBTabbedPane();

    var nameCyclingTab = FormBuilder.createFormBuilder()
        .addComponent(new JBLabel("Enabled naming schemes:"))
        .addComponentToRightColumn(pascalCaseCheckbox)
        .addComponentToRightColumn(camelCaseCheckbox)
        .addComponentToRightColumn(dotCaseCheckbox)
        .addComponentToRightColumn(doenerCaseCheckbox)
        .addComponentToRightColumn(snakeCaseCheckbox)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();

    var general = FormBuilder.createFormBuilder()
        .addComponent(staticImports)
        .addComponent(introduceMockFieldPrivateField)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();

    var multiLineMagic = FormBuilder.createFormBuilder()
        .addLabeledComponent(new JBLabel("Default start value: "), multilineMagicStartValue, 1, false)
        .addLabeledComponent(new JBLabel("Default interval value: "), multilineMagicIntervalValue, 1, false)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();

    var codeLense = FormBuilder.createFormBuilder()
        .addLabeledComponent(new JBLabel("CodeLense font size: "), codeLenseFontSizeField, 1, false)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();

    tabbedPane.addTab("General", general);
    tabbedPane.addTab("NameCycling", nameCyclingTab);
    tabbedPane.addTab("MultiLineMagic", multiLineMagic);
    tabbedPane.addTab("CodeLense", codeLense);
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
    return pascalCaseCheckbox.isSelected();
  }

  public void setpascalCaseImpl(boolean selected) {
    pascalCaseCheckbox.setSelected(selected);
  }

  public Boolean getcamelCaseImpl() {
    return camelCaseCheckbox.isSelected();
  }

  public void setcamelCaseImpl(boolean selected) {
    camelCaseCheckbox.setSelected(selected);
  }

  public Boolean getdotCaseImpl() {
    return dotCaseCheckbox.isSelected();
  }

  public void setdotCaseImpl(boolean selected) {
    dotCaseCheckbox.setSelected(selected);
  }

  public Boolean getdoenerCaseImpl() {
    return doenerCaseCheckbox.isSelected();
  }

  public void setdoenerCaseImpl(boolean selected) {
    doenerCaseCheckbox.setSelected(selected);
  }

  public Boolean getsnakeCaseImpl() {
    return snakeCaseCheckbox.isSelected();
  }

  public void setsnakeCaseImpl(boolean selected) {
    snakeCaseCheckbox.setSelected(selected);
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

  public Integer getCodeLenseFontSize() {
    return Integer.parseInt(codeLenseFontSizeField.getText());
  }

  public void setCodeLenseFontSize(Integer fontSize) {
    codeLenseFontSizeField.setText(String.valueOf(fontSize));
  }

  public void setMultiLineMagicStartValue(Integer newMmStartValue) {
    multilineMagicStartValue.setText(String.valueOf(newMmStartValue));
  }

  public void setMultiLineMagicIntervalValue(Integer newMmIntervalValue) {
    multilineMagicIntervalValue.setText(String.valueOf(newMmIntervalValue));
  }

}