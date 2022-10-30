package io.github.askmeagain.more.shortcuts.introducemock;

import com.intellij.openapi.actionSystem.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.util.PsiTreeUtil;
import io.github.askmeagain.more.shortcuts.introducemock.entities.ExececutionExtractor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

public class SmartIntroduceVariableAction extends AnAction {

  @Override
  @SneakyThrows
  public void actionPerformed(@NotNull AnActionEvent e) {

    var editor = e.getRequiredData(CommonDataKeys.EDITOR);
    var psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE);
    var project = e.getRequiredData(CommonDataKeys.PROJECT);
    var dataContext = e.getDataContext();

    int offset = editor.getCaretModel().getOffset();
    var element = psiFile.findElementAt(offset);

    //find list
    var expressionList = findPsiExpressionList(element);

    //find possible implementations
    if (SmartIntroduceUtils.isMethod(expressionList)) {
      int i = 0;
    } else if (SmartIntroduceUtils.isConstructor(expressionList)) {
      var paramsFromConstructor = SmartIntroduceUtils.getPsiParametersFromConstructor(expressionList);
      SmartIntroduceUtils.createContextMenu(ExececutionExtractor.Constructor, paramsFromConstructor, editor, dataContext, e, offset);
    } else {
      throw new RuntimeException("Could not determine if Constructor or Method.");
    }
  }

  private PsiExpressionList findPsiExpressionList(PsiElement element) {
    var expressionList = PsiTreeUtil.getParentOfType(element, PsiExpressionList.class);
    if (expressionList != null) {
      return expressionList;
    }

    throw new RuntimeException("Could not find psiExpression list");
  }
}
