package io.github.askmeagain.more.shortcuts.introducemock2.impl;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiJavaToken;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static io.github.askmeagain.more.shortcuts.introducemock2.SmartIntroduceUtils.toCamelCase;

public class SmartIntroduceParameterAction extends AnAction {

  private final PsiParameter[] parameterList;
  private final Integer textOffset;

  public SmartIntroduceParameterAction(PsiParameter[] parameterList, Integer textOffset) {
    super("Parameter");
    this.parameterList = parameterList;
    this.textOffset = textOffset;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var psiClass = e.getRequiredData(CommonDataKeys.PSI_FILE)
        .findElementAt(textOffset)
        .getParent()
        .getParent()
        .getParent()
        .getParent()
        .getParent();

    var finalString = Arrays.stream(parameterList).map(p -> p.getType().getPresentableText() + " " + p.getName()).collect(Collectors.joining(", "));
    int realTextOffset = PsiTreeUtil.findChildOfType(psiClass, PsiParameterList.class).getTextOffset() + 1;

    var document = e.getRequiredData(CommonDataKeys.EDITOR).getDocument();

    WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
      //the variables
      document.insertString(textOffset, Arrays.stream(parameterList)
          .map(x -> toCamelCase(x.getName()))
          .collect(Collectors.joining(", ")));
      document.insertString(realTextOffset, finalString);

      PsiDocumentManager.getInstance(e.getProject()).commitDocument(document);
      new ReformatCodeProcessor(e.getRequiredData(CommonDataKeys.PSI_FILE), false).run();
    });
  }
}
