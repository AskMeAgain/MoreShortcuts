package io.github.askmeagain.more.shortcuts.introducemock.impl;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiParameter;
import io.github.askmeagain.more.shortcuts.settings.PersistenceManagementService;

import java.util.ArrayList;

public class SmartIntroduceMockVariableAction extends SmartIntroduceBaseClass {

  private final PsiParameter[] newParameters;
  private final Boolean isMock;
  private final Integer textOffset;
  private final PsiExpressionList oldExpressionList;

  private static final String TEMPLATE = "$FULLCLAZZ $NAME = $IMPORT$TYPE($CLAZZ.class);";

  public SmartIntroduceMockVariableAction(
      PsiExpressionList oldExpressionList,
      Boolean isMock,
      PsiParameter[] newParameters,
      Integer textOffset
  ) {
    super(isMock ? "Mock to Variable" : "Spy to Variable");
    this.isMock = isMock;
    this.textOffset = textOffset;
    this.newParameters = newParameters;
    this.oldExpressionList = oldExpressionList;
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    var result = new ArrayList<String>();

    var state = PersistenceManagementService.getInstance().getState();

    for (int i = 0; i < newParameters.length; i++) {
      if (oldExpressionList.getExpressionCount() == 0 || oldExpressionList.getExpressionTypes()[i].equalsToText("null")) {
        var param = newParameters[i];
        var s = TEMPLATE.replace("$NAME", param.getName())
            .replace("$FULLCLAZZ", state.getPreferVar() ? "var " : param.getType().getPresentableText() + " ")
            .replace("$CLAZZ", extractClass(param))
            .replace("$TYPE", isMock ? "mock" : "spy")
            .replace("$IMPORT", state.getStaticImports() ? "" : "Mockito.");
        result.add(s);
      }
    }

    var finalString = "\n" + String.join("\n", result) + "\n\n";
    var realTextOffset = e.getRequiredData(CommonDataKeys.PSI_FILE)
        .findElementAt(textOffset)
        .getParent()
        .getParent()
        .getParent()
        .getTextOffset() - 2;

    var document = e.getRequiredData(CommonDataKeys.EDITOR).getDocument();

    WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
      //the variables
      addParameterToParameterList(document, textOffset, newParameters, oldExpressionList);
      document.insertString(realTextOffset, finalString);

      if (state.getStaticImports()) {
        addImport(document, "import static org.mockito." + (isMock ? "mock" : "spy") + ";");
      } else {
        addImport(document, "import org.mockito.Mockito;");
      }

      reformatCode(e);
    });
  }

  private static String extractClass(PsiParameter param) {
    var text = param.getType().getPresentableText();

    if (text.contains("<")) {
      return text.substring(0, text.indexOf("<"));
    }

    return text;
  }
}
