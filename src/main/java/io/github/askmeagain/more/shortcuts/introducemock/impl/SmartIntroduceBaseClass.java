package io.github.askmeagain.more.shortcuts.introducemock.impl;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.impl.source.PsiClassImpl;
import io.github.askmeagain.more.shortcuts.introducemock.SmartIntroduceUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
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

  protected void addParameterToParameterList(Document document, Integer textOffset, PsiParameter[] parameterList) {
    document.insertString(textOffset, Arrays.stream(parameterList)
        .map(x -> toCamelCase(x.getName()))
        .collect(Collectors.joining(", ")));
  }

  protected void addImport(Document document, PsiElement element, String text) {

    var psiClass = SmartIntroduceUtils.findRecursivelyInParent(element, PsiClassImpl.class);

    document.insertString(document.getLineEndOffset(1), text);
  }

}
