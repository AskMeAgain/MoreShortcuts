package IntroduceMockFieldTest.Abc;

import io.github.askmeagain.more.shortcuts.introducemock.entities.TestClass;

class IntroduceMockFieldExample {

  private TestClass testClass = new TestClass();

  void example() {
    testClass.nested(<caret>null,null);
  }
}
