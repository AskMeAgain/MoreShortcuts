package io.github.askmeagain.more.shortcuts.insertionpopup;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.EditorTextField;
import io.github.askmeagain.more.shortcuts.settings.MoreShortcutState;
import io.github.askmeagain.more.shortcuts.settings.PersistenceManagementService;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
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

    var cursorOffset = calculateNewOffset(editor);

    System.out.println("Initial offset:" + cursorOffset);

    editorTextField.setCaretPosition(cursorOffset);

    init();
  }

  public void setCursorToNextEntity(int lineOffset) {
    currentCursorIndex.addAndGet(lineOffset);

    var editor = editorTextField.getEditor();
    var cursorOffset = calculateNewOffset(editor);

    var newLogicalPosition = editor.offsetToLogicalPosition(cursorOffset);

    editor.getScrollingModel().scrollTo(newLogicalPosition, ScrollType.CENTER);
    editorTextField.setCaretPosition(cursorOffset);
  }

  private int calculateNewOffset(Editor editor) {
    var indexOfList = calculateOffsetInList();

    var logicalPosition = editor.offsetToLogicalPosition(indexOfList);
    var newLine = logicalPosition.line + cursorLineOffset;

    return editor.getDocument().getLineEndOffset(newLine);
  }

  private Integer calculateOffsetInList() {
    var listSize = this.cursorOffsets.size();

    while (this.currentCursorIndex.get() < 0) {
      this.currentCursorIndex.addAndGet(listSize);
    }

    var finalIndex = this.currentCursorIndex.get() % listSize;

    return this.cursorOffsets.get(finalIndex);
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
