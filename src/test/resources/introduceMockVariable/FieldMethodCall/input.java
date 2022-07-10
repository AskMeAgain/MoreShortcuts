package IntroduceMockFieldTest.Abc;

import io.github.askmeagain.more.shortcuts.introducemock.entities.TestClass;

class IntroduceMockVariableExample {

  private TestClass testClass= new TestClass();

  void example() {
    var result = testClass.nested(<caret>null,null);
  }
}