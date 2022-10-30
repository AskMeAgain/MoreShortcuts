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
  private final PsiParameter[] parameterList;
  private static final String TEMPLATE = "@TYPE\nprivate $CLAZZ $NAME;";

  private final Integer textOffset;

  public SmartIntroduceMockFieldAction(Boolean isMock, PsiParameter[] parameterList, Integer textOffset) {
    super(isMock ? "Mock to Field" : "Spy to Field");
    this.isMock = isMock;
    this.parameterList = parameterList;
    this.textOffset = textOffset;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var result = new ArrayList<String>();

    for (var param : parameterList) {
      var s = TEMPLATE.replace("$NAME", param.getName())
          .replace("$CLAZZ", param.getType().getPresentableText())
          .replace("TYPE", isMock ? "Mock" : "Spy");
      result.add(s);
    }

    var finalString = "\n\n" + String.join("\n", result);

    var psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE);

    var elementAt = psiFile.findElementAt(textOffset);
    var psiClass = SmartIntroduceUtils.findRecursivelyInParent(elementAt, PsiClassImpl.class);

    var realTextOffset = PsiTreeUtil.findChildrenOfType(psiClass, PsiJavaToken.class)
        .stream()
        .filter(x -> x.getTokenType().getIndex() == 288) //Left bracket
        .findFirst()
        .get()
        .getTextOffset() + 1;

    var document = e.getRequiredData(CommonDataKeys.EDITOR).getDocument();

    WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
      addParameterToParameterList(document, textOffset, parameterList);
      document.insertString(realTextOffset, finalString);
      addImport(document, "import org.mockito." + (isMock ? "Mock" : "Spy") + ";");

      reformatCode(e);
    });
  }
}
