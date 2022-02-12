package ask.me.again.shortcut.additions.smartpaste;

import ask.me.again.shortcut.additions.smartpaste.insertions.*;
import com.intellij.ide.CopyPasteManagerEx;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.codeStyle.CodeStyleManager;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.DataFlavor;
import java.util.List;

public class SmartPasteAction extends AnAction {

  private static List<SmartInsertion> smartInsertions = List.of(
      new BracketsAtEndInsertion(),
      new MethodTargetInsertion(),
      new MethodOriginInsertion(),
      new NormalPasteInsertions()
  );

  @Override
  @SneakyThrows
  public void actionPerformed(@NotNull AnActionEvent e) {

    var clipBoard = (String) CopyPasteManagerEx.getInstance().getContents().getTransferData(DataFlavor.stringFlavor);

    var editor = e.getRequiredData(CommonDataKeys.EDITOR);
    var document = editor.getDocument();

    var project = e.getProject();
    var psiFile = e.getData(CommonDataKeys.PSI_FILE);
    var codeStyleManager = CodeStyleManager.getInstance(project);

    editor.getCaretModel().runForEachCaret(caret -> {

      var start = caret.getSelectionStart();
      var end = caret.getSelectionEnd();
      var text = document.getText(TextRange.from(start, end - start));

      var newText = smartInsertions.stream()
          .filter(x -> x.isApplicable(text, clipBoard))
          .findFirst()
          .get()
          .apply(text, clipBoard);

      WriteCommandAction.runWriteCommandAction(project, () -> {
        document.replaceString(start, end, newText);
        codeStyleManager.reformatText(psiFile, start, start + newText.length());
      });
    });
  }
}

