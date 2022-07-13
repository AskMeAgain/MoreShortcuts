package io.github.askmeagain.more.shortcuts.insertionpopup;

import com.intellij.codeInsight.hint.HintManager;
import com.intellij.codeInsight.hint.HintManagerImpl;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;

public class InsertionPopupEscapeHandler extends EditorActionHandler {
  private final EditorActionHandler myOriginalHandler;

  public static InsertionPopupAction.MultilineCountDialog editorTextField;

  public InsertionPopupEscapeHandler(EditorActionHandler originalHandler) {
    myOriginalHandler = originalHandler;
  }

  @Override
  public void doExecute(Editor editor, Caret caret, DataContext dataContext) {
    myOriginalHandler.execute(editor, caret, dataContext);
    if (editorTextField != null) {
      editorTextField.close(0);
      editorTextField = null;
    }
  }
}