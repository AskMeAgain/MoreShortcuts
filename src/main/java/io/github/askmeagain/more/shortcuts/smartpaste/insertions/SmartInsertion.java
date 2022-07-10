package io.github.askmeagain.more.shortcuts.smartpaste.insertions;

import com.intellij.openapi.actionSystem.AnActionEvent;

public interface SmartInsertion {

  boolean isApplicable(String originalText, String clipboard, AnActionEvent anActionEvent);

  String apply(String originalText, String clipboard, AnActionEvent e);

}
