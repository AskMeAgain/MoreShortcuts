package io.github.askmeagain.more.shortcuts.mockswitchtype.impl;

import io.github.askmeagain.more.shortcuts.commons.CommonPsiUtils;
import io.github.askmeagain.more.shortcuts.mockswitchtype.SwitchMockingVariantUtils;
import io.github.askmeagain.more.shortcuts.settings.MoreShortcutState;
import io.github.askmeagain.more.shortcuts.settings.PersistenceManagementService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.codeStyle.CodeStyleManager;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

public class SwitchMockingVariantAction extends AnAction {

  private final MoreShortcutState state = PersistenceManagementService.getInstance().getState();

  @SneakyThrows
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    // Get all the required data from data keys
    var editor = e.getRequiredData(CommonDataKeys.EDITOR);
    var project = e.getRequiredData(CommonDataKeys.PROJECT);
    var document = editor.getDocument();
    var codeStyleManager = CodeStyleManager.getInstance(project);
    var psiFile = e.getData(CommonDataKeys.PSI_FILE);

    editor.getCaretModel().runForEachCaret(caret -> {

      var logicalPosition = caret.getLogicalPosition();
      var line = logicalPosition.line;

      var start = document.getLineStartOffset(line);
      var end = document.getLineEndOffset(line);

      var text = editor.getDocument().getText(TextRange.from(start, end - start));
      var newText = SwitchMockingVariantUtils.convertLine(text, state.getStaticImports());

      WriteCommandAction.runWriteCommandAction(project, () -> {
        document.replaceString(start, end, newText);
        codeStyleManager.reformatText(psiFile, start, start + newText.length());
      });
    });

    if (state.getStaticImports()) {
      CommonPsiUtils.addStaticImports(psiFile, project, "when", "doReturn");
    }
  }
}
