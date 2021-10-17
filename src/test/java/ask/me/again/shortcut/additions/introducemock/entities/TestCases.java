package ask.me.again.shortcut.additions.introducemock.entities;

import org.mockito.Mockito;

class TestCases {

  private TestClass testClass;

  void simpleIntroduceMock() {
    testClass.exampleMethod(1, null, null);
  }

  void context() {
    var string = Mockito.mock(String.class);
    testClass.context(1, string);
  }

  void contextMenuVariable() {
    var testClass1 = new TestClass(1, null, null);
  }

  void staticClass() {
    TestClass.staticMethod(0, null);
  }

  void builderClass() {
    testClass.nested(1, "")
        .nested(1, "")
        .nested(1, "")
        .nested(1, "")
        .nested(null, null);
  }

  void builderClassAssignment() {
    testClass = testClass.nested(1, "")
        .nested(1, "")
        .nested(1, "")
        .nested(1, "")
        .nested(null, null);
  }

}