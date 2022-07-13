package io.github.askmeagain.more.shortcuts.insertionpopup;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.ui.EditorTextField;
import io.github.askmeagain.more.shortcuts.settings.MoreShortcutState;
import io.github.askmeagain.more.shortcuts.settings.PersistenceManagementService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class InsertionPopupAction extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    InsertionPopupEscapeHandler.editorTextField = new MultilineCountDialog(e);
    InsertionPopupEscapeHandler.editorTextField.show();
  }

  public static class MultilineCountDialog extends DialogWrapper {

    public final EditorTextField editorTextField;

    private final MoreShortcutState state = PersistenceManagementService.getInstance().getState();

    public MultilineCountDialog(AnActionEvent e) {
      super(null, null, true, IdeModalityType.IDE, false);

      setTitle("CodeLense");
      setSize(700, 200);
      setModal(false);

      var editor = e.getRequiredData(CommonDataKeys.EDITOR);

      var document = editor.getDocument();
      var project = e.getProject();

      editorTextField = new EditorTextField(document, project, JavaFileType.INSTANCE);

      e.getRequiredData(CommonDataKeys.PSI_FILE).accept(new JavaRecursiveElementVisitor() {
        @Override
        public void visitClass(PsiClass clazz) {
          var logicalPosition = editor.offsetToLogicalPosition(clazz.getTextOffset());
          var newLine = logicalPosition.line - 1;
          var newLogicalPosition = new LogicalPosition(newLine, 0);

          editorTextField.setCaretPosition(editor.logicalPositionToOffset(newLogicalPosition));
        }
      });

      init();
    }

    @Override
    protected @NotNull DialogStyle getStyle(){
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
