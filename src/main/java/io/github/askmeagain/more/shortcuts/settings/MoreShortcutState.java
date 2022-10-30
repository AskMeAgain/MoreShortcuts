package io.github.askmeagain.more.shortcuts.settings;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoreShortcutState {

  Integer mMStartValue = 1;
  Integer mMIntervalValue = 1;
  Boolean staticImports = false;
  Boolean introduceMockFieldPrivateField = false;

  Boolean pascalCaseImpl = true;
  Boolean camelCaseImpl = true;
  Boolean dotCaseImpl = true;
  Boolean doenerCaseImpl = true;
  Boolean snakeCaseImpl = true;

  Boolean preferVar = true;

  Integer codeLenseFontSize = 15;

}