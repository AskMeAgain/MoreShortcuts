package io.github.askmeagain.more.shortcuts.introducemock2.impl;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiParameter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static io.github.askmeagain.more.shortcuts.introducemock2.SmartIntroduceUtils.toCamelCase;

public class SmartIntroduceMockVariableAction extends SmartIntroduceBaseClass {

  private final PsiParameter[] parameterList;
  private Boolean isMock;
  private final Integer textOffset;

  private static final String TEMPLATE = "var $NAME = Mockito.$TYPE($CLAZZ.class);";

  public SmartIntroduceMockVariableAction(Boolean isMock, PsiParameter[] parameterList, Integer textOffset) {
    super(isMock ? "Mock to Variable" : "Spy to Variable");
    this.isMock = isMock;
    this.textOffset = textOffset;
    this.parameterList = parameterList;
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    var result = new ArrayList<String>();

    for (var param : parameterList) {
      var s = TEMPLATE.replace("$NAME", param.getName())
          .replace("$CLAZZ", param.getType().getPresentableText())
          .replace("$TYPE", isMock ? "mock" : "spy");
      result.add(s);
    }

    var finalString = "\n" + String.join("\n", result) + "\n\n";
    var realTextOffset = e.getRequiredData(CommonDataKeys.PSI_FILE)
        .findElementAt(textOffset)
        .getParent()
        .getParent()
        .getTextOffset() - 2;

    var document = e.getRequiredData(CommonDataKeys.EDITOR).getDocument();

    WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
      //the variables
      addParameterToParameterList(document, textOffset, parameterList);
      document.insertString(realTextOffset, finalString);

      reformatCode(e);
    });
  }
}
