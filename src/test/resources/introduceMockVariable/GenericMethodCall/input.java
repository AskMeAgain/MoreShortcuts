package IntroduceMockFieldTest.Abc;

import io.github.askmeagain.more.shortcuts.introducemock.entities.TestClass;

class IntroduceGenericVariableExample {
  void example() {
    var testClass = new TestClass();
    var result = testClass.testMethodGeneric(<caret>null);
  }
}