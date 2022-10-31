package io.github.askmeagain.more.shortcuts.introducemock;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiParameter;
import io.github.askmeagain.more.shortcuts.introducemock.entities.ExececutionExtractor;
import io.github.askmeagain.more.shortcuts.introducemock.impl.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.intellij.openapi.ui.popup.JBPopupFactory.ActionSelectionAid.SPEEDSEARCH;

public class SmartIntroduceListDisplayAction extends AnAction {

  static int testIndex = 0;
  private final ExececutionExtractor execution;
  private final PsiParameter[] newParameters;
  private final Integer textOffset;
  private final PsiExpressionList oldExpressionList;

  public SmartIntroduceListDisplayAction(
      PsiExpressionList oldExpressionList,
      ExececutionExtractor execution,
      PsiParameter[] newParameters,
      Integer textOffset
  ) {
    super(Arrays.stream(newParameters)
        .map(PsiParameter::getName)
        .collect(Collectors.joining(", ")));

    this.oldExpressionList = oldExpressionList;
    this.execution = execution;
    this.newParameters = newParameters;
    this.textOffset = textOffset;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var actionGroup = new DefaultActionGroup();
    var editor = e.getRequiredData(CommonDataKeys.EDITOR);

    actionGroup.add(new SmartIntroduceMockFieldAction(oldExpressionList,true, newParameters, textOffset));
    actionGroup.add(new SmartIntroduceMockVariableAction(oldExpressionList,true, newParameters, textOffset));
    actionGroup.add(new SmartIntroduceParameterAction(oldExpressionList,newParameters, textOffset));
    actionGroup.add(new SmartIntroduceMockFieldAction(oldExpressionList,false, newParameters, textOffset));
    actionGroup.add(new SmartIntroduceMockVariableAction(oldExpressionList,false, newParameters, textOffset));

    if (!ApplicationManager.getApplication().isUnitTestMode()) {
      JBPopupFactory.getInstance()
          .createActionGroupPopup(null, actionGroup, e.getDataContext(), SPEEDSEARCH, false)
          .showInBestPositionFor(editor);
    } else {
      actionGroup.getChildActionsOrStubs()[testIndex].actionPerformed(e);
    }
  }
}
