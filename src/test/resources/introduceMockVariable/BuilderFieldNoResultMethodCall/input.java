package IntroduceMockFieldTest.Abc;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;

class IntroduceMockVariableExample {

  private final TestClass testClass = new TestClass();

  void example() {
    testClass.nested(1, "abc")
        .nested(1, "abc")
        .nested(<caret>null, null);
  }
}