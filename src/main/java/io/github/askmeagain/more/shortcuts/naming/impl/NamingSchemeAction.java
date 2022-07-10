package io.github.askmeagain.more.shortcuts.naming.impl;

import io.github.askmeagain.more.shortcuts.naming.service.NamingSchemeService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

public class NamingSchemeAction extends AnAction {

  @VisibleForTesting
  public static int INDEX = 0;

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var editor = e.getRequiredData(CommonDataKeys.EDITOR);
    var document = editor.getDocument();

    var project = e.getProject();
    var service = new NamingSchemeService();

    ++INDEX;

    editor.getCaretModel().runForEachCaret(caret -> {

      var start = caret.getSelectionStart();
      var end = caret.getSelectionEnd();
      var text = document.getText(TextRange.from(start, end - start));

      WriteCommandAction.runWriteCommandAction(project, () -> {
            var newText = service.applyNext(text, INDEX);
            document.replaceString(start, end, newText);
          }
      );
    });
  }
}
