package io.github.askmeagain.more.shortcuts.insertionpopup;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.ui.EditorTextField;
import io.github.askmeagain.more.shortcuts.settings.MoreShortcutState;

import javax.swing.*;
import java.awt.*;

public class CodeLenseUtils {

  public static JPanel createPanel(JComponent buttonToolBar, JComponent textEditor) {
    var panel = new JPanel(new GridBagLayout());
    var gbc = new GridBagConstraints();

    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.weighty = 0;

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    panel.add(buttonToolBar, gbc);

    gbc.weighty = 1;
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridheight = 100;
    gbc.fill = GridBagConstraints.BOTH;
    panel.add(textEditor, gbc);

    panel.setPreferredSize(new Dimension(0, 200));

    return panel;
  }

  public static JComponent setupTextEditor(EditorTextField editorTextField, MoreShortcutState state) {
    editorTextField.setOneLineMode(false);
    editorTextField.setSize(1000, 1000);

    var origFont = editorTextField.getFont();
    var newFont = new Font(origFont.getName(), origFont.getStyle(), state.getCodeLenseFontSize());

    editorTextField.setFont(newFont);
    editorTextField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    return editorTextField;
  }

  public static JComponent createButtonToolBar() {
    var parent = new JPanel();

    var instance = ActionManager.getInstance();
    var actionGroup = (ActionGroup) instance.getAction("CodeLense");
    var toolbar = instance.createActionToolbar("abc", actionGroup, true);
    toolbar.setTargetComponent(parent);
    return toolbar.getComponent();
  }
}
