package IntroduceMockFieldTest.Abc;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;

class IntroduceGenericVariableExample {
  void example() {
    var testClass = new TestClass();
    var result = testClass.testMethodGeneric(<caret>null);
  }
}