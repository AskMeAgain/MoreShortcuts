package io.github.askmeagain.more.shortcuts.insertionpopup.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import io.github.askmeagain.more.shortcuts.insertionpopup.CodeLenseService;
import lombok.Getter;

import java.util.List;

public abstract class BaseInsertionPopupAction extends AnAction {

  @Getter(lazy = true)
  private final CodeLenseService codeLenseService = ApplicationManager.getApplication().getService(CodeLenseService.class);

  protected void openDialog(AnActionEvent e, List<Integer> offsets, int lineOffset, Document document) {
    getCodeLenseService().openDialog(e, offsets, lineOffset, document);
  }
}
