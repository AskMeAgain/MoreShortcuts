package IntroduceMockFieldTest.Abc;

import io.github.askmeagain.more.shortcuts.introducemock.entities.TestClass;

class IntroduceMockFieldExample {

  private final TestClass testClass = new TestClass();

  void example() {
    testClass.nested(1, "abc")
        .nested(1, "abc")
        .nested(<caret>null, null);
  }
}