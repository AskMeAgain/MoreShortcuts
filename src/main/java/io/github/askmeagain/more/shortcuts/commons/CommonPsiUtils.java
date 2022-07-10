package io.github.askmeagain.more.shortcuts.commons;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiImportList;
import com.intellij.psi.util.PsiTreeUtil;

public class CommonPsiUtils {

  public static void addStaticImports(PsiFile psiFile, Project project, String... imports) {
    var factory = JavaPsiFacade.getElementFactory(project);
    var base = PsiHelpers.getClassFromString(project, "org.mockito.Mockito");

    WriteCommandAction.runWriteCommandAction(project, () -> {
      var importList = PsiTreeUtil.getChildOfType(psiFile, PsiImportList.class);
      if (importList != null) {
        for (String anImport : imports) {
          importList.add(factory.createImportStaticStatement(base, anImport));
        }
      }

      new ReformatCodeProcessor(psiFile, false).run();
    });
  }
}
