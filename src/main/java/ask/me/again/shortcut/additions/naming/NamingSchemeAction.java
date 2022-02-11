package ask.me.again.shortcut.additions.naming;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

public class NamingSchemeAction extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var editor = e.getRequiredData(CommonDataKeys.EDITOR);
    var document = editor.getDocument();

    var project = e.getProject();

    editor.getCaretModel().runForEachCaret(caret -> {

      var start = caret.getSelectionStart();
      var end = caret.getSelectionEnd();
      var text = document.getText(TextRange.from(start, end - start));

      WriteCommandAction.runWriteCommandAction(project, () -> {
            var schemeIndex = NamingSchemeUtils.findScheme(text);
            var newText = NamingSchemeUtils.applyNext(text, schemeIndex);
            document.replaceString(start, end, newText);
          }
      );
    });
  }
}
