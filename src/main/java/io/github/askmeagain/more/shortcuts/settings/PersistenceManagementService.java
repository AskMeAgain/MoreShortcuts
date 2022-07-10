package io.github.askmeagain.more.shortcuts.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;

@Service
@State(name = "MoreShortcuts", storages = @Storage("more-shortcuts.xml"))
public final class PersistenceManagementService implements PersistentStateComponent<MoreShortcutState> {

  private MoreShortcutState moreShortcutState = new MoreShortcutState();

  @Override
  public @NotNull MoreShortcutState getState() {
    return moreShortcutState;
  }

  @Override
  public void loadState(@NotNull MoreShortcutState state) {
    moreShortcutState = state;
  }

  public static PersistenceManagementService getInstance() {
    return ApplicationManager.getApplication().getService(PersistenceManagementService.class);
  }
}