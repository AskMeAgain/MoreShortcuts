package io.github.askmeagain.more.shortcuts.introducemock.actions;

import io.github.askmeagain.more.shortcuts.introducemock.IntroduceMockImpl;
import io.github.askmeagain.more.shortcuts.introducemock.entities.ExecutionTarget;
import io.github.askmeagain.more.shortcuts.introducemock.entities.MockType;

public class IntroduceMockToVariable extends IntroduceMockImpl {
  protected IntroduceMockToVariable() {
    super(ExecutionTarget.Variable, MockType.mock);
  }
}
