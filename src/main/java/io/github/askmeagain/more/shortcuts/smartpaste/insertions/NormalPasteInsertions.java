package io.github.askmeagain.more.shortcuts.smartpaste.insertions;

import com.intellij.openapi.actionSystem.AnActionEvent;

public class NormalPasteInsertions implements SmartInsertion {
  @Override
  public boolean isApplicable(String originalText, String text, AnActionEvent e) {
    return true;
  }

  @Override
  public String apply(String originalText, String clipboard, AnActionEvent e) {
    return clipboard;
  }
}
