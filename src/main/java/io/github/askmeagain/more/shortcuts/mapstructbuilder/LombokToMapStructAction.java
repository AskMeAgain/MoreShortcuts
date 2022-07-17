package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClassOwner;
import com.intellij.psi.PsiDeclarationStatement;
import com.intellij.psi.impl.PsiJavaParserFacadeImpl;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;

public class LombokToMapStructAction extends AnAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {

    var editor = e.getRequiredData(CommonDataKeys.EDITOR);
    var psiFile = e.getData(CommonDataKeys.PSI_FILE);

    editor.getCaretModel().runForEachCaret(caret -> {

      int offset = caret.getOffset();
      var element = psiFile.findElementAt(offset);

      var packageName = ((PsiClassOwner) psiFile).getPackageName();

      var visitor = new Visitor(packageName);
      PsiTreeUtil.getParentOfType(element, PsiDeclarationStatement.class).accept(visitor);
      var result = visitor.getResult().toString();

      System.out.println(result);
    });
  }
}
