package io.github.askmeagain.more.shortcuts.insertionpopup;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.EditorTextField;
import io.github.askmeagain.more.shortcuts.settings.MoreShortcutState;
import io.github.askmeagain.more.shortcuts.settings.PersistenceManagementService;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.css.Rect;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CodeLenseWindow extends DialogWrapper {

  private final EditorTextField editorTextField;
  private final List<Integer> cursorOffsets;
  private final Integer cursorLineOffset;
  private final AtomicInteger currentCursorIndex = new AtomicInteger();

  @Getter(lazy = true)
  private final MoreShortcutState state = PersistenceManagementService.getInstance().getState();

  public CodeLenseWindow(AnActionEvent e, List<Integer> cursorOffsets, int lineOffset, Document document) {
    super(null, null, true, IdeModalityType.IDE, false);
    this.cursorOffsets = cursorOffsets;
    this.cursorLineOffset = lineOffset;

    setTitle("CodeLense");
    setSize(700, 200);
    setModal(false);

    var editor = e.getRequiredData(CommonDataKeys.EDITOR);
    var project = e.getProject();

    editorTextField = new EditorTextField(document, project, JavaFileType.INSTANCE);

    var newLogicalPosition = calculateNewLogicalPosition(editor);

    editorTextField.setCaretPosition(editor.logicalPositionToOffset(newLogicalPosition));

    init();
  }

  private LogicalPosition calculateNewLogicalPosition(Editor editor) {
    var logicalPosition = editor.offsetToLogicalPosition(this.cursorOffsets.get(currentCursorIndex.get() % this.cursorOffsets.size()));
    var newLine = logicalPosition.line + cursorLineOffset;
    return new LogicalPosition(newLine, 2);
  }

  public void setCursorToNextEntity(int lineOffset) {
    currentCursorIndex.addAndGet(lineOffset);

    var editor = editorTextField.getEditor();
    var newLogicalPosition = calculateNewLogicalPosition(editor);

    var editorOffset = editor.logicalPositionToOffset(newLogicalPosition);
    editor.getScrollingModel().scrollTo(newLogicalPosition, ScrollType.CENTER);
    editorTextField.setCaretPosition(editorOffset);
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
    return CodeLenseUtils.createPanel(getState(), editorTextField);
  }
}
