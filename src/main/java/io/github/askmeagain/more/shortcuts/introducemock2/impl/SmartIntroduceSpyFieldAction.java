package io.github.askmeagain.more.shortcuts.introducemock2.impl;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import org.jetbrains.annotations.NotNull;

public class SmartIntroduceSpyFieldAction extends AnAction {

  private final PsiParameter[] parameterList;

  public SmartIntroduceSpyFieldAction(PsiParameter[] parameterList) {
    super("Spy to Field");
    this.parameterList = parameterList;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {

  }
}