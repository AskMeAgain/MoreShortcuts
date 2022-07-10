package io.github.askmeagain.more.shortcuts.settings;

import com.intellij.openapi.options.Configurable;
import lombok.Getter;
import org.jetbrains.annotations.Nls;

import javax.swing.*;

public class SettingsGuiManager implements Configurable {

  @Getter(lazy = true)
  private final PersistenceManagementService persistenceManagementService = PersistenceManagementService.getInstance();
  private MoreShortcutsSettingsWindow settingsComponent;

  @Nls(capitalization = Nls.Capitalization.Title)
  @Override
  public String getDisplayName() {
    return "More Shortcuts Settings";
  }

  @Override
  public JComponent getPreferredFocusedComponent() {
    return settingsComponent.getPreferredFocusedComponent();
  }

  @Override
  public JComponent createComponent() {
    var state = getPersistenceManagementService().getState();

    settingsComponent = new MoreShortcutsSettingsWindow();

    settingsComponent.setMultiLineMagicStartValue(state.getMMStartValue());
    settingsComponent.setMultiLineMagicIntervalValue(state.getMMIntervalValue());
    settingsComponent.setStaticImports(state.getStaticImports());
    settingsComponent.setIntroduceMockFieldPrivateField(state.getIntroduceMockFieldPrivateField());

    settingsComponent.setdoenerCaseImpl(state.getDoenerCaseImpl());
    settingsComponent.setsnakeCaseImpl(state.getSnakeCaseImpl());
    settingsComponent.setcamelCaseImpl(state.getCamelCaseImpl());
    settingsComponent.setdotCaseImpl(state.getDotCaseImpl());
    settingsComponent.setpascalCaseImpl(state.getPascalCaseImpl());

    return settingsComponent.getPanel();
  }

  @Override
  public boolean isModified() {
    var state = getPersistenceManagementService().getState();

    var startValueChanged = !settingsComponent.getMultiLineMagicStartValue().equals(state.getMMStartValue());
    var intervalValueChanged = !settingsComponent.getMultiLineMagicIntervalValue().equals(state.getMMIntervalValue());
    var staticImportsChanged = !settingsComponent.getStaticImports().equals(state.getStaticImports());
    var privateFieldChanged = !settingsComponent.getIntroduceMockFieldPrivateField().equals(state.getIntroduceMockFieldPrivateField());

    var doenerCaseChanged = !settingsComponent.getdoenerCaseImpl().equals(state.getDoenerCaseImpl());
    var pascalCaseChanged = !settingsComponent.getpascalCaseImpl().equals(state.getPascalCaseImpl());
    var camelCaseChanged = !settingsComponent.getcamelCaseImpl().equals(state.getCamelCaseImpl());
    var snakeCasechanged = !settingsComponent.getsnakeCaseImpl().equals(state.getSnakeCaseImpl());
    var dotCaseChanged = !settingsComponent.getdotCaseImpl().equals(state.getDotCaseImpl());

    return startValueChanged
        || intervalValueChanged
        || staticImportsChanged
        || privateFieldChanged
        || doenerCaseChanged
        || pascalCaseChanged
        || camelCaseChanged
        || snakeCasechanged
        || dotCaseChanged;
  }

  @Override
  public void apply() {
    var state = getPersistenceManagementService().getState();

    state.setMMStartValue(settingsComponent.getMultiLineMagicStartValue());
    state.setMMIntervalValue(settingsComponent.getMultiLineMagicIntervalValue());
    state.setStaticImports(settingsComponent.getStaticImports());
    state.setIntroduceMockFieldPrivateField(settingsComponent.getIntroduceMockFieldPrivateField());

    state.setDoenerCaseImpl(settingsComponent.getdoenerCaseImpl());
    state.setPascalCaseImpl(settingsComponent.getpascalCaseImpl());
    state.setCamelCaseImpl(settingsComponent.getcamelCaseImpl());
    state.setSnakeCaseImpl(settingsComponent.getsnakeCaseImpl());
    state.setDotCaseImpl(settingsComponent.getdotCaseImpl());
  }

  @Override
  public void reset() {
//    var state = getPersistenceManagementService().getState();
//
//    state.setMMStartValue(1);
//    state.setMMIntervalValue(1);
//    state.setStaticImports(false);
  }

  @Override
  public void disposeUIResources() {
    settingsComponent = null;
  }

}