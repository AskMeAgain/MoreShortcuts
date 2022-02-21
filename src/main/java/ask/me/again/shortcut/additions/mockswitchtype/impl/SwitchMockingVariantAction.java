package ask.me.again.shortcut.additions.mockswitchtype.impl;

import ask.me.again.shortcut.additions.commons.CommonPsiUtils;
import ask.me.again.shortcut.additions.mockswitchtype.SwitchMockingVariantUtils;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.codeStyle.CodeStyleManager;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import static ask.me.again.shortcut.additions.mockswitchtype.settings.SwitchMockVariantSettingsPanel.IMPORT_FLAG;
import static ask.me.again.shortcut.additions.settings.SettingsUtils.computeName;

public class SwitchMockingVariantAction extends AnAction {

  @SneakyThrows
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    // Get all the required data from data keys
    var editor = e.getRequiredData(CommonDataKeys.EDITOR);
    var project = e.getRequiredData(CommonDataKeys.PROJECT);
    var document = editor.getDocument();
    var codeStyleManager = CodeStyleManager.getInstance(project);
    var psiFile = e.getData(CommonDataKeys.PSI_FILE);
    var addStaticImport = PropertiesComponent.getInstance(e.getProject())
        .getBoolean(computeName(IMPORT_FLAG), false);

    editor.getCaretModel().runForEachCaret(caret -> {

      var logicalPosition = caret.getLogicalPosition();
      var line = logicalPosition.line;

      var start = document.getLineStartOffset(line);
      var end = document.getLineEndOffset(line);

      var text = editor.getDocument().getText(TextRange.from(start, end - start));
      var newText = SwitchMockingVariantUtils.convertLine(text, addStaticImport);

      WriteCommandAction.runWriteCommandAction(project, () -> {
        document.replaceString(start, end, newText);
        codeStyleManager.reformatText(psiFile, start, start + newText.length());
      });
    });

    if (addStaticImport) {
      CommonPsiUtils.addStaticImports(psiFile, project, "when", "doReturn");
    }
  }
}
