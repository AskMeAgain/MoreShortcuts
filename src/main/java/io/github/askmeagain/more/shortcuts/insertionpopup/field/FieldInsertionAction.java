package io.github.askmeagain.more.shortcuts.insertionpopup.field;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import io.github.askmeagain.more.shortcuts.insertionpopup.BaseInsertionPopupAction;
import org.jetbrains.annotations.NotNull;

public class FieldInsertionAction extends BaseInsertionPopupAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    e.getRequiredData(CommonDataKeys.PSI_FILE).accept(new JavaRecursiveElementVisitor() {
      @Override
      public void visitField(PsiField field) {
        openDialog(e, field.getTextOffset(), 1, e.getRequiredData(CommonDataKeys.EDITOR).getDocument());
      }
    });
  }

}
