package io.github.askmeagain.more.shortcuts.introducemock.impl;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.askmeagain.more.shortcuts.introducemock.SmartIntroduceUtils.toCamelCase;

public abstract class SmartIntroduceBaseClass extends AnAction {
  public SmartIntroduceBaseClass(String text) {
    super(text);
  }

  protected static void reformatCode(@NotNull AnActionEvent e) {
    var requiredData = e.getRequiredData(CommonDataKeys.PSI_FILE);
    var editor = e.getRequiredData(CommonDataKeys.EDITOR);

    PsiDocumentManager.getInstance(e.getProject()).commitDocument(editor.getDocument());
    new ReformatCodeProcessor(requiredData, false).run();
  }

  protected void addParameterToParameterList(
      Document document,
      Integer textOffset,
      PsiParameter[] parameterList,
      PsiExpressionList oldExpressionList
  ) {
    if (oldExpressionList.isEmpty()) {
      var result = Arrays.stream(parameterList)
          .map(x -> toCamelCase(x.getName()))
          .collect(Collectors.joining(", "));

      document.insertString(textOffset, result);
    } else {
      var expressionTypes = oldExpressionList.getExpressionTypes();
      var result = new ArrayList<String>();

      for (int i = 0; i < parameterList.length; i++) {
        if (expressionTypes[i].equalsToText("null")) {
          result.add(parameterList[i].getName());
        } else {
          result.add(oldExpressionList.getExpressions()[i].getText());
        }
      }

      var resultString = "(" + String.join(", ", result) + ")";
      var textRange = oldExpressionList.getTextRange();
      document.replaceString(textRange.getStartOffset(), textRange.getEndOffset(), resultString);
    }
  }

  protected void addImport(Document document, String text) {
    document.insertString(document.getLineEndOffset(1), text);
  }

}
