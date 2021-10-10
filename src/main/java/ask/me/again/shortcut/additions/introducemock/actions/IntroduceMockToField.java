package ask.me.again.shortcut.additions.introducemock.actions;

import ask.me.again.shortcut.additions.introducemock.IntroduceMockImpl;
import ask.me.again.shortcut.additions.introducemock.entities.ExecutionTarget;

public class IntroduceMockToField extends IntroduceMockImpl {
  protected IntroduceMockToField() {
    super(ExecutionTarget.Field);
  }
}
