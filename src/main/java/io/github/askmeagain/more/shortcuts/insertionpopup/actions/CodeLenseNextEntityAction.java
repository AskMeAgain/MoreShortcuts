package io.github.askmeagain.more.shortcuts.insertionpopup.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import io.github.askmeagain.more.shortcuts.insertionpopup.CodeLenseService;
import io.github.askmeagain.more.shortcuts.insertionpopup.CodeLenseWindow;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;


public class CodeLenseNextEntityAction extends AnAction {

  @Getter(lazy = true)
  private final CodeLenseService codeLenseService = ApplicationManager.getApplication().getService(CodeLenseService.class);

  @Override
  public void actionPerformed(AnActionEvent e) {
    getCodeLenseService().moveCursorToNextEntity(e);
  }
}
