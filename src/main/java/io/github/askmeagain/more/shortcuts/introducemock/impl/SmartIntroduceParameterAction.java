package io.github.askmeagain.more.shortcuts.introducemock.impl;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.impl.source.PsiMethodImpl;
import com.intellij.psi.util.PsiTreeUtil;
import io.github.askmeagain.more.shortcuts.introducemock.SmartIntroduceUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SmartIntroduceParameterAction extends SmartIntroduceBaseClass {

  private final PsiParameter[] parameterList;
  private final Integer textOffset;
  private final PsiExpressionList oldExpressionList;

  public SmartIntroduceParameterAction(
      PsiExpressionList oldExpressionList,
      PsiParameter[] parameterList,
      Integer textOffset
  ) {
    super("Parameter");
    this.parameterList = parameterList;
    this.textOffset = textOffset;
    this.oldExpressionList = oldExpressionList;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var psiMethod = SmartIntroduceUtils.findRecursivelyInParent(e.getRequiredData(CommonDataKeys.PSI_FILE).findElementAt(textOffset), PsiMethodImpl.class);

    var finalString = Arrays.stream(parameterList).map(p -> p.getType().getPresentableText() + " " + p.getName()).collect(Collectors.joining(", "));
    int realTextOffset = PsiTreeUtil.findChildOfType(psiMethod, PsiParameterList.class).getTextOffset() + 1;

    var document = e.getRequiredData(CommonDataKeys.EDITOR).getDocument();

    WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
      addParameterToParameterList(document, textOffset, parameterList, oldExpressionList);

      document.insertString(realTextOffset, finalString);

      reformatCode(e);
    });
  }
}
