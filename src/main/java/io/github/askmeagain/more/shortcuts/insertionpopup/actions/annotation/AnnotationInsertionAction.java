package io.github.askmeagain.more.shortcuts.insertionpopup.actions.annotation;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiClass;
import io.github.askmeagain.more.shortcuts.insertionpopup.actions.BaseInsertionPopupAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AnnotationInsertionAction extends BaseInsertionPopupAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var textOffsets = new ArrayList<Integer>();

    e.getRequiredData(CommonDataKeys.PSI_FILE).accept(new JavaRecursiveElementVisitor() {
      @Override
      public void visitClass(PsiClass clazz) {
        textOffsets.add(clazz.getTextOffset());
        super.visitClass(clazz);
      }
    });

    openDialog(e, textOffsets, -1, e.getRequiredData(CommonDataKeys.EDITOR).getDocument());
  }

}
