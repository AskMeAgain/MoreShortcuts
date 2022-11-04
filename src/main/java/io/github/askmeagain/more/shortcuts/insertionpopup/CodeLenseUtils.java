package io.github.askmeagain.more.shortcuts.insertionpopup;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.ui.EditorTextField;
import io.github.askmeagain.more.shortcuts.settings.MoreShortcutState;

import javax.swing.*;
import java.awt.*;

public class CodeLenseUtils {

  public static JPanel createPanel(MoreShortcutState state, EditorTextField editorTextField) {
    var panel = new JPanel(new BorderLayout());

    panel.add(setupTextEditor(editorTextField, state), BorderLayout.CENTER);
    panel.add(createButtonToolBar(panel), BorderLayout.LINE_END);
    panel.setPreferredSize(new Dimension(1000, 200));

    return panel;
  }

  private static JComponent setupTextEditor(EditorTextField editorTextField, MoreShortcutState state) {
    editorTextField.setOneLineMode(false);
    editorTextField.setSize(1000, 1000);

    var origFont = editorTextField.getFont();
    var newFont = new Font(origFont.getName(), origFont.getStyle(), state.getCodeLenseFontSize());

    editorTextField.setFont(newFont);
    editorTextField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));

    return editorTextField;
  }

  private static JComponent createButtonToolBar(JPanel parent) {
    var instance = ActionManager.getInstance();
    var actionGroup = (ActionGroup) instance.getAction("CodeLenseToolWindow");
    var toolbar = instance.createActionToolbar("abc", actionGroup, false);
    toolbar.setTargetComponent(parent);
    var component = toolbar.getComponent();
    component.setPreferredSize(new Dimension(50, 200));
    return component;
  }
}
