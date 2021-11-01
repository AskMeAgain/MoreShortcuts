package IntroduceMockFieldTest.Abc;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;
import org.junit.Test;

class IntroduceMockVariableExample {
  void example() {
    var result = TestClass.staticMethod2(<caret>null,null);
  }
}