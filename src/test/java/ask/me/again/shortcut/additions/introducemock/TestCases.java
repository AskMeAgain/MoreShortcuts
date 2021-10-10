package ask.me.again.shortcut.additions.introducemock;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;

class TestCases {

  private TestClass testClass;

  void simpleIntroduceMock() {
    testClass.asd(null, null, null);
  }

  void contextMenuVariable() {
    var testClass1 = new TestClass(1, null, null);
  }

  void contextMenuField() {
    testClass = new TestClass(1, null, null);
  }

  void staticClass() {
    TestClass.testAsd(0, null);
  }

  void builderClass() {
    testClass.nested(1, "")
        .nested(1, "")
        .nested(1, "")
        .nested(1, "")
        .nested(null, null);
  }

}