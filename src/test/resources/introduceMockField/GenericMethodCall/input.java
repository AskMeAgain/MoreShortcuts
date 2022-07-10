package IntroduceMockFieldTest.Abc;

import io.github.askmeagain.more.shortcuts.introducemock.entities.TestClass;

class IntroduceGenericMockFieldExample {
  void example() {
    var testClass = new TestClass();
    var result = testClass.testMethodGeneric(<caret>null);
  }
}
