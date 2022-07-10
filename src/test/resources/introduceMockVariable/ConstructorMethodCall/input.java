package ask.me.again.shortcut.additions.introducemock.introducemockfield;

import io.github.askmeagain.more.shortcuts.introducemock.entities.TestClass;

class IntroduceMockVariableExample {
  void example() {
    var result = new TestClass().nested(<caret>null,null);
  }
}