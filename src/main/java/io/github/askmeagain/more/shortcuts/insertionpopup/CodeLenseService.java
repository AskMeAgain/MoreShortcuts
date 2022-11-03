package io.github.askmeagain.more.shortcuts.insertionpopup;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
public final class CodeLenseService {

  @Getter(lazy = true)
  private final EditorActionManager editorActionManager = EditorActionManager.getInstance();

  private CodeLenseWindow codeLensePopupDialog;

  public void moveCursorToNextEntity() {
    codeLensePopupDialog.setCursorToNextEntity(1);
  }

  public void moveCursorToPreviousEntity() {
    codeLensePopupDialog.setCursorToNextEntity(-1);
  }

  public void openDialog(AnActionEvent e, List<Integer> offsets, int lineOffset, Document document) {
    codeLensePopupDialog = new CodeLenseWindow(e, offsets, lineOffset, document);
    codeLensePopupDialog.show();

    var oldHandler = getEditorActionManager().getActionHandler(IdeActions.ACTION_EDITOR_ESCAPE);

    getEditorActionManager().setActionHandler(
        IdeActions.ACTION_EDITOR_ESCAPE,
        new InsertionPopupEscapeHandler(
            codeLensePopupDialog,
            () -> getEditorActionManager().setActionHandler(IdeActions.ACTION_EDITOR_ESCAPE, oldHandler)
        )
    );
  }

  @RequiredArgsConstructor
  public static class InsertionPopupEscapeHandler extends EditorActionHandler {

    public final CodeLenseWindow codeLenseWindow;
    private final Runnable action;

    @Override
    public void doExecute(Editor editor, Caret caret, DataContext dataContext) {
      codeLenseWindow.close(0);
      action.run();
    }
  }
}
