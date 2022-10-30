package io.github.askmeagain.more.shortcuts.insertionpopup.annotation;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiClass;
import io.github.askmeagain.more.shortcuts.insertionpopup.BaseInsertionPopupAction;
import org.jetbrains.annotations.NotNull;

public class AnnotationInsertionAction extends BaseInsertionPopupAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    e.getRequiredData(CommonDataKeys.PSI_FILE).accept(new JavaRecursiveElementVisitor() {
      @Override
      public void visitClass(PsiClass clazz) {
        openDialog(e, clazz.getTextOffset(), - 1, e.getRequiredData(CommonDataKeys.EDITOR).getDocument());
      }
    });
  }

}
