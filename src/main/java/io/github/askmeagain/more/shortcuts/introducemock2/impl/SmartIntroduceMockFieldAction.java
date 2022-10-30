package io.github.askmeagain.more.shortcuts.introducemock2.impl;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaToken;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import io.github.askmeagain.more.shortcuts.commons.PsiHelpers;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static io.github.askmeagain.more.shortcuts.introducemock2.SmartIntroduceUtils.toCamelCase;

public class SmartIntroduceMockFieldAction extends AnAction {

  private final PsiParameter[] parameterList;
  private static final String TEMPLATE = "@Mock\nprivate $CLAZZ $NAME;";

  private final Integer textOffset;

  public SmartIntroduceMockFieldAction(PsiParameter[] parameterList, Integer textOffset) {
    super("Mock to Field");
    this.parameterList = parameterList;
    this.textOffset = textOffset;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var result = new ArrayList<String>();

    for (var param : parameterList) {
      var s = TEMPLATE.replace("$NAME", param.getName())
          .replace("$CLAZZ", param.getType().getPresentableText());
      result.add(s);
    }

    var finalString = "\n\n" + String.join("\n", result);

    var psiClass = e.getRequiredData(CommonDataKeys.PSI_FILE)
        .findElementAt(textOffset)
        .getParent()
        .getParent()
        .getParent()
        .getParent()
        .getParent()
        .getParent();

    var realTextOffset = PsiTreeUtil.findChildrenOfType(psiClass, PsiJavaToken.class)
        .stream()
        .filter(x -> x.getTokenType().getIndex() == 288)
        .findFirst()
        .get()
        .getTextOffset() + 1;

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
