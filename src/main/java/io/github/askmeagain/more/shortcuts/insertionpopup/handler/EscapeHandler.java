package io.github.askmeagain.more.shortcuts.insertionpopup.handler;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import io.github.askmeagain.more.shortcuts.insertionpopup.CodeLenseWindow;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EscapeHandler extends EditorActionHandler {

  public final CodeLenseWindow codeLenseWindow;
  private final Runnable action;

  @Override
  public void doExecute(Editor editor, Caret caret, DataContext dataContext) {
    codeLenseWindow.close(0);
    action.run();
  }
}
