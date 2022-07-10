package io.github.askmeagain.more.shortcuts.smartpaste.insertions;

import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.yaml.YAMLUtil;
import org.jetbrains.yaml.psi.YAMLFile;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.*;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class YmlInsertion implements SmartInsertion {

  @Override
  public boolean isApplicable(String originalText, String clipboardText, AnActionEvent e) {
    var psiFile = e.getData(CommonDataKeys.PSI_FILE);
    if (psiFile == null) {
      return false;
    }

    var fileName = psiFile.getName();

    return fileName.endsWith(".yml") || fileName.endsWith(".yaml");
  }

  @Override
  public String apply(String originalText, String clipboard, AnActionEvent e) {
    var psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE);

    var splitted = clipboard.split("=");
    var stack = new LinkedList<>(List.of(splitted[0].split("\\.")));

    var i = 0;
    YAMLKeyValue result = null;
    for (i = 1; i < stack.size(); i++) {
      var temp = YAMLUtil.getQualifiedKeyInFile((YAMLFile) psiFile, stack.subList(0, i));
      if (temp == null) {
        break;
      }
      result = temp;
    }

    var testblock = createYamlBlock("testblock", e);
    var root = PsiTreeUtil.findChildOfType(testblock, YAMLBlockMappingImpl.class);
    result.getParent().getParent().add(root);

    return "";
  }

  private PsiFile createYamlBlock(String blockName, AnActionEvent e) {
    return PsiFileFactory.getInstance(e.getProject()).createFileFromText(Language.findLanguageByID("yaml"), blockName + ":\n");
  }


}
