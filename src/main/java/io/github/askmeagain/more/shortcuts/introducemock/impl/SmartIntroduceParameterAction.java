package io.github.askmeagain.more.shortcuts.introducemock.impl;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.impl.source.PsiMethodImpl;
import com.intellij.psi.util.PsiTreeUtil;
import io.github.askmeagain.more.shortcuts.introducemock.SmartIntroduceUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SmartIntroduceParameterAction extends SmartIntroduceBaseClass {

  private final PsiParameter[] newParameters;
  private final Integer textOffset;
  private final PsiExpressionList oldExpressionList;

  public SmartIntroduceParameterAction(
      PsiExpressionList oldExpressionList,
      PsiParameter[] newParameters,
      Integer textOffset
  ) {
    super("Parameter");
    this.newParameters = newParameters;
    this.textOffset = textOffset;
    this.oldExpressionList = oldExpressionList;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var psiMethod = SmartIntroduceUtils.findRecursivelyInParent(e.getRequiredData(CommonDataKeys.PSI_FILE).findElementAt(textOffset), PsiMethodImpl.class);

    var existingMethodParams = Arrays.stream(psiMethod.getParameterList().getParameters());

    var resultString = Stream.concat(existingMethodParams, Arrays.stream(newParameters))
        .map(p -> p.getType().getPresentableText() + " " + p.getName())
        .distinct()
        .collect(Collectors.joining(", "));

    var textRange = PsiTreeUtil.findChildOfType(psiMethod, PsiParameterList.class).getTextRange();

    var document = e.getRequiredData(CommonDataKeys.EDITOR).getDocument();

    WriteCommandAction.runWriteCommandAction(e.getProject(), () ->
    {
      addParameterToParameterList(document, textOffset, newParameters, oldExpressionList);

      document.replaceString(textRange.getStartOffset() + 1, textRange.getEndOffset() - 1, resultString);

      reformatCode(e);
    });
  }
}
