package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClassOwner;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LombokToMapStructAction extends AnAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {

    var editor = e.getRequiredData(CommonDataKeys.EDITOR);
    var document = editor.getDocument();
    var project = e.getProject();
    var psiFile = e.getData(CommonDataKeys.PSI_FILE);
    var data = e.getData(PlatformDataKeys.VIRTUAL_FILE);

    editor.getCaretModel().runForEachCaret(caret -> {
      var start = caret.getSelectionStart();
      var end = caret.getSelectionEnd();
      var text = document.getText(TextRange.from(start, end - start));

      var packageName = ((PsiClassOwner) psiFile).getPackageName();

      var lines = text.split("\n");

      var result = LombokToMapStructUtils.buildMapper(lines, packageName);
      var name = LombokToMapStructUtils.findOutputType(lines[0]) + "Mapper.java";

      WriteCommandAction.runWriteCommandAction(project, () -> {
        try {
          var resultFile = data.getParent().createChildData(null, name);
          resultFile.setBinaryContent(result.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      });
    });
  }
}
