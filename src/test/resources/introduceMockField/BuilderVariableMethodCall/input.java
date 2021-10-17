package IntroduceMockFieldTest.Abc;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;

class IntroduceMockFieldExample {
  void example() {
    var testClass = new TestClass();
    var result = testClass
        .nested(1,"abc")
        .nested(1,"abc")
        .nested(<caret>null,null);
  }
}