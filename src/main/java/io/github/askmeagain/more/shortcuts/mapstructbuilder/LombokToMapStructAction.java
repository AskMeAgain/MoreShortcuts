package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiClassOwner;
import com.intellij.psi.PsiDeclarationStatement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LombokToMapStructAction extends AnAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {

    var editor = e.getRequiredData(CommonDataKeys.EDITOR);
    var psiFile = e.getData(CommonDataKeys.PSI_FILE);
    var project = e.getProject();
    var data = e.getData(PlatformDataKeys.VIRTUAL_FILE);

    editor.getCaretModel().runForEachCaret(caret -> {

      int offset = caret.getOffset();
      var element = psiFile.findElementAt(offset);

      var packageName = ((PsiClassOwner) psiFile).getPackageName();

      var visitor = new LombokToMapStructVisitor(packageName);
      PsiTreeUtil.getParentOfType(element, PsiDeclarationStatement.class).accept(visitor);
      var result = visitor.getResult();

      var name = result.getOutputType().getPresentableText() + "Mapper.java";

      WriteCommandAction.runWriteCommandAction(project, () -> {
        try {
          data.getParent()
              .createChildData(null, name)
              .setBinaryContent(result.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      });
    });
  }
}
