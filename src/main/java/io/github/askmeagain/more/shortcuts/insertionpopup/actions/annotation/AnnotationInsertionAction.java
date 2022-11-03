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

  private final List<Integer> textOffsets = new ArrayList<>();

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    e.getRequiredData(CommonDataKeys.PSI_FILE).accept(new JavaRecursiveElementVisitor() {
      @Override
      public void visitClass(PsiClass clazz) {
        textOffsets.add(clazz.getTextOffset());
      }
    });

    openDialog(e, textOffsets, -1, e.getRequiredData(CommonDataKeys.EDITOR).getDocument());
  }

}
