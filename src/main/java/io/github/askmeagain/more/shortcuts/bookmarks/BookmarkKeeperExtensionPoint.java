package io.github.askmeagain.more.shortcuts.bookmarks;

import com.intellij.openapi.project.Project;
import com.intellij.tasks.LocalTask;
import com.intellij.tasks.actions.vcs.VcsTaskDialogPanelProvider;
import com.intellij.tasks.ui.TaskDialogPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BookmarkKeeperExtensionPoint extends VcsTaskDialogPanelProvider {

  @Nullable
  @Override
  public TaskDialogPanel getOpenTaskPanel(@NotNull Project project, @NotNull LocalTask task) {
    return new BookmarkKeeperPanel(project);
  }

  @Nullable
  @Override
  public TaskDialogPanel getCloseTaskPanel(@NotNull Project project, @NotNull LocalTask task) {
    return new BookmarkKeeperPanel(project);
  }
}
