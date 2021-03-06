package io.github.askmeagain.more.shortcuts.mockswitchtype;

import com.google.common.base.Strings;
import io.github.askmeagain.more.shortcuts.commons.CommonPsiUtils;
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

      var text = caret.getSelectedText();
      var start = caret.getSelectionStart();
      var end = caret.getSelectionEnd();

      if(Strings.isNullOrEmpty(text)) {
        var logicalPosition = caret.getLogicalPosition();
        var line = logicalPosition.line;

        start = document.getLineStartOffset(line);
        end = document.getLineEndOffset(line);

        text = editor.getDocument().getText(TextRange.from(start, end - start));
      } else {
        text = text.replace("\n","");
      }

      var newText = SwitchMockingVariantUtils.convertLine(text, state.getStaticImports());

      int finalStart = start;
      int finalEnd = end;

      WriteCommandAction.runWriteCommandAction(project, () -> {
        document.replaceString(finalStart, finalEnd, newText);
        codeStyleManager.reformatText(psiFile, finalStart, finalStart + newText.length());
      });
    });

    if (state.getStaticImports()) {
      CommonPsiUtils.addStaticImports(psiFile, project, "when", "doReturn");
    }
  }
}
