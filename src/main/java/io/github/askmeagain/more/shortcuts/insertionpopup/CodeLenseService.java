package io.github.askmeagain.more.shortcuts.insertionpopup;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import io.github.askmeagain.more.shortcuts.insertionpopup.handler.EscapeHandler;
import io.github.askmeagain.more.shortcuts.insertionpopup.handler.PageDownHandler;
import io.github.askmeagain.more.shortcuts.insertionpopup.handler.PageUpHandler;
import lombok.Getter;

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

    var actionManager = getEditorActionManager();

    var escapeHandler = actionManager.getActionHandler(IdeActions.ACTION_EDITOR_ESCAPE);
    var pageDownHandler = actionManager.getActionHandler(IdeActions.ACTION_EDITOR_MOVE_CARET_PAGE_DOWN);
    var pageUpHandler = actionManager.getActionHandler(IdeActions.ACTION_EDITOR_MOVE_CARET_PAGE_UP);

    actionManager.setActionHandler(IdeActions.ACTION_EDITOR_MOVE_CARET_PAGE_DOWN, new PageDownHandler(codeLensePopupDialog));
    actionManager.setActionHandler(IdeActions.ACTION_EDITOR_MOVE_CARET_PAGE_UP, new PageUpHandler(codeLensePopupDialog));

    actionManager.setActionHandler(
        IdeActions.ACTION_EDITOR_ESCAPE,
        new EscapeHandler(
            codeLensePopupDialog,
            () -> {
              actionManager.setActionHandler(IdeActions.ACTION_EDITOR_ESCAPE, escapeHandler);
              actionManager.setActionHandler(IdeActions.ACTION_EDITOR_MOVE_CARET_PAGE_DOWN, pageDownHandler);
              actionManager.setActionHandler(IdeActions.ACTION_EDITOR_MOVE_CARET_PAGE_UP, pageUpHandler);
            }
        )
    );
  }
}
