package io.github.askmeagain.more.shortcuts.introducemock2.impl;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import org.jetbrains.annotations.NotNull;

public class SmartIntroduceParameterAction extends AnAction {

  private final PsiParameter[] parameterList;

  public SmartIntroduceParameterAction(PsiParameter[] parameterList) {
    super("Parameter");
    this.parameterList = parameterList;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {

  }
}
