package io.github.askmeagain.more.shortcuts.insertionpopup.actions.field;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiField;
import io.github.askmeagain.more.shortcuts.insertionpopup.actions.BaseInsertionPopupAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FieldInsertionAction extends BaseInsertionPopupAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var textOffsets = new ArrayList<Integer>();

    e.getRequiredData(CommonDataKeys.PSI_FILE).accept(new JavaRecursiveElementVisitor() {
      @Override
      public void visitField(PsiField field) {
        textOffsets.add(field.getTextOffset());
        super.visitField(field);
      }
    });

    openDialog(e, textOffsets, 0, e.getRequiredData(CommonDataKeys.EDITOR).getDocument());
  }

}
