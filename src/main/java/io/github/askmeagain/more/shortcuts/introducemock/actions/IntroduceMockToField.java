package io.github.askmeagain.more.shortcuts.introducemock.actions;

import io.github.askmeagain.more.shortcuts.introducemock.IntroduceMockImpl;
import io.github.askmeagain.more.shortcuts.introducemock.entities.ExecutionTarget;
import io.github.askmeagain.more.shortcuts.introducemock.entities.MockType;

public class IntroduceMockToField extends IntroduceMockImpl {
  public IntroduceMockToField() {
    super(ExecutionTarget.Field, MockType.mock);
  }
}
