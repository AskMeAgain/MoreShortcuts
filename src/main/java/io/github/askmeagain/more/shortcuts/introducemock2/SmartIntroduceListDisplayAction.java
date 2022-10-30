package io.github.askmeagain.more.shortcuts.introducemock2;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.PsiParameter;
import io.github.askmeagain.more.shortcuts.introducemock.entities.ExececutionExtractor;
import io.github.askmeagain.more.shortcuts.introducemock2.impl.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.intellij.openapi.ui.popup.JBPopupFactory.ActionSelectionAid.SPEEDSEARCH;

public class SmartIntroduceListDisplayAction extends AnAction {

  private final ExececutionExtractor execution;
  private final PsiParameter[] selection;
  private final Integer textOffset;

  public SmartIntroduceListDisplayAction(ExececutionExtractor execution, PsiParameter[] selection, Integer textOffset) {
    super(Arrays.stream(selection).map(PsiParameter::getName).collect(Collectors.joining(", ")));
    this.execution = execution;
    this.selection = selection;
    this.textOffset = textOffset;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var actionGroup = new DefaultActionGroup();
    var editor = e.getRequiredData(CommonDataKeys.EDITOR);

    actionGroup.add(new SmartIntroduceMockFieldAction(selection, textOffset));
    actionGroup.add(new SmartIntroduceMockVariableAction(selection, textOffset));
    actionGroup.add(new SmartIntroduceParameterAction(selection));
    actionGroup.add(new SmartIntroduceSpyFieldAction(selection));
    actionGroup.add(new SmartIntroduceSpyVariableAction(selection));

    if (!ApplicationManager.getApplication().isUnitTestMode()) {
      JBPopupFactory.getInstance()
          .createActionGroupPopup(null, actionGroup, e.getDataContext(), SPEEDSEARCH, false)
          .showInBestPositionFor(editor);
    } else {
      actionGroup.getChildActionsOrStubs()[0].actionPerformed(e);
    }
  }
}
