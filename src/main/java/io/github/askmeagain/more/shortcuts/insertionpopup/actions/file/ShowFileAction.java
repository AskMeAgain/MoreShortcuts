package io.github.askmeagain.more.shortcuts.insertionpopup.actions.file;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiTypeElementImpl;
import io.github.askmeagain.more.shortcuts.commons.PsiHelpers;
import io.github.askmeagain.more.shortcuts.insertionpopup.actions.BaseInsertionPopupAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShowFileAction extends BaseInsertionPopupAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var textOffsets = new ArrayList<Integer>();

    var editor = e.getRequiredData(CommonDataKeys.EDITOR);
    var psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE);

    int offset = editor.getCaretModel().getOffset();
    var element = psiFile.findElementAt(offset);
    var project = e.getProject();

    var type = ((PsiTypeElementImpl) element.getParent().getParent()).getType();

    var containingFile = PsiHelpers.getClassFromString(project, type.getCanonicalText()).getContainingFile();

    containingFile.accept(new JavaRecursiveElementVisitor() {
      @Override
      public void visitClass(PsiClass clazz) {
        textOffsets.add(clazz.getTextOffset());
      }
    });

    openDialog(e, textOffsets, 1, PsiDocumentManager.getInstance(project).getDocument(containingFile));

  }

}
