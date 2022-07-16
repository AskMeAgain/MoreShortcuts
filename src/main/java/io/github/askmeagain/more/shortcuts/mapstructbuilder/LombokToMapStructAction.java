package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClassOwner;
import com.intellij.psi.impl.PsiJavaParserFacadeImpl;
import org.jetbrains.annotations.NotNull;

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

      var visitor = new Visitor(packageName, psiFile);
      new PsiJavaParserFacadeImpl(project).createStatementFromText(text, null).accept(visitor);
      var result = visitor.getResult().toString();

      System.out.println(result);
    });
  }
}
