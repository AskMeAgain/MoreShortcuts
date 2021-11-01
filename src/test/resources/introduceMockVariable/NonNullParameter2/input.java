package IntroduceMockFieldTest.Abc;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;
import org.mockito.Mock;

class IntroduceMockVariableExample {
  void example() {
    var testClass = new TestClass();
    var result = testClass.nested(<caret>1, null);
  }
}