package ask.me.again.shortcut.additions.introducemock.actions;

import ask.me.again.shortcut.additions.introducemock.IntroduceMockImpl;
import ask.me.again.shortcut.additions.introducemock.entities.ExecutionTarget;

public class IntroduceMockToVariable extends IntroduceMockImpl {
  protected IntroduceMockToVariable() {
    super(ExecutionTarget.Variable);
  }
}
