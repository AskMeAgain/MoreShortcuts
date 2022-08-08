package io.github.askmeagain.more.shortcuts.insertionpopup;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InsertionPopupEscapeHandler extends EditorActionHandler {

  public final BaseInsertionPopupAction.CodeLenseWindow editorTextField;
  private final Runnable action;

  @Override
  public void doExecute(Editor editor, Caret caret, DataContext dataContext) {
    editorTextField.close(0);
    action.run();
  }
}