package io.github.askmeagain.more.shortcuts.introducemock.impl;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassImpl;
import com.intellij.psi.util.PsiTreeUtil;
import io.github.askmeagain.more.shortcuts.introducemock.SmartIntroduceUtils;
import io.github.askmeagain.more.shortcuts.settings.PersistenceManagementService;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SmartIntroduceMockFieldAction extends SmartIntroduceBaseClass {

  private final Boolean isMock;
  private final PsiParameter[] newParameters;
  private static final String TEMPLATE = "  @TYPE\n$PRIVATE_FIELD$CLAZZ $NAME;";
  private final PsiExpressionList oldExpressionList;

  private final Integer textOffset;

  public SmartIntroduceMockFieldAction(
      PsiExpressionList oldExpressionList,
      Boolean isMock,
      PsiParameter[] newParameters,
      Integer textOffset
  ) {
    super(isMock ? "Mock to Field" : "Spy to Field");
    this.isMock = isMock;
    this.newParameters = newParameters;
    this.textOffset = textOffset;
    this.oldExpressionList = oldExpressionList;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var result = new ArrayList<String>();

    for (int i = 0; i < newParameters.length; i++) {
      if (oldExpressionList.getExpressionCount() == 0 || oldExpressionList.getExpressionTypes()[i].equalsToText("null")) {
        var param = newParameters[i];
        var s = TEMPLATE.replace("$NAME", param.getName())
            .replace("$CLAZZ", param.getType().getPresentableText())
            .replace("TYPE", isMock ? "Mock" : "Spy")
            .replace("$PRIVATE_FIELD", PersistenceManagementService.getInstance().getState().getIntroduceMockFieldPrivateField() ? "private " : "");
        result.add(s);
      }
    }

    var finalString = "\n\n" + String.join("\n", result);

    var psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE);

    var elementAt = psiFile.findElementAt(textOffset);
    var psiClass = SmartIntroduceUtils.findRecursivelyInParent(elementAt, PsiClassImpl.class);

    var realTextOffset = PsiTreeUtil.findChildrenOfType(psiClass, PsiJavaToken.class)
        .stream()
        .filter(x -> x.getTokenType().getDebugName().equals("LBRACE")) //Left bracket
        .findFirst()
        .get()
        .getTextOffset() + 1;

    var document = e.getRequiredData(CommonDataKeys.EDITOR).getDocument();

    WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
      addParameterToParameterList(document, textOffset, newParameters, oldExpressionList);
      document.insertString(realTextOffset, finalString);
      addImport(document, "import org.mockito." + (isMock ? "Mock" : "Spy") + ";");

      reformatCode(e);
    });
  }
}
