package io.github.askmeagain.more.shortcuts.insertionpopup;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.EditorTextField;
import io.github.askmeagain.more.shortcuts.settings.MoreShortcutState;
import io.github.askmeagain.more.shortcuts.settings.PersistenceManagementService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class BaseInsertionPopupAction extends AnAction {

  private final EditorActionManager editorActionManager = EditorActionManager.getInstance();

  protected void openDialog(AnActionEvent e, int offset, int lineOffset) {

    var editorTextField = new CodeLenseWindow(e, offset, lineOffset);
    editorTextField.show();

    var oldHandler = editorActionManager.getActionHandler(IdeActions.ACTION_EDITOR_ESCAPE);

    editorActionManager.setActionHandler(
        IdeActions.ACTION_EDITOR_ESCAPE,
        new InsertionPopupEscapeHandler(
            editorTextField,
            () -> editorActionManager.setActionHandler(IdeActions.ACTION_EDITOR_ESCAPE, oldHandler))
    );
  }

  public void actionPerformed(@NotNull AnActionEvent e) {
  }

  public static class CodeLenseWindow extends DialogWrapper {

    public final EditorTextField editorTextField;

    private final MoreShortcutState state = PersistenceManagementService.getInstance().getState();

    public CodeLenseWindow(AnActionEvent e, int cursorOffset, int lineOffset) {
      super(null, null, true, IdeModalityType.IDE, false);

      setTitle("CodeLense");
      setSize(700, 200);
      setModal(false);

      var editor = e.getRequiredData(CommonDataKeys.EDITOR);

      var document = editor.getDocument();
      var project = e.getProject();

      var logicalPosition = editor.offsetToLogicalPosition(cursorOffset);
      var newLine = logicalPosition.line + lineOffset;
      var newLogicalPosition = new LogicalPosition(newLine, 0);

      editorTextField = new EditorTextField(document, project, JavaFileType.INSTANCE);
      editorTextField.setCaretPosition(editor.logicalPositionToOffset(newLogicalPosition));
      init();
    }

    @Override
    protected @NotNull DialogStyle getStyle() {
      return DialogStyle.COMPACT;
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
      return editorTextField;
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {

      editorTextField.setOneLineMode(false);
      editorTextField.setSize(1000, 1000);

      var origFont = editorTextField.getFont();
      var newFont = new Font(origFont.getName(), origFont.getStyle(), state.getCodeLenseFontSize());

      editorTextField.setFont(newFont);
      editorTextField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

      return editorTextField;
    }
  }
}
