package IntroduceMockFieldTest.Abc;

import io.github.askmeagain.more.shortcuts.introducemock.entities.TestClass;

class IntroduceMockVariableExample {
  void example() {
    var testClass = new TestClass();
    testClass.nested(1, "abc")
        .nested(1, "abc")
        .nested(<caret>null, null);
  }
}