package ask.me.again.shortcut.additions.introducemock.entities;

class TestCases {

  private TestClass testClass;

  void simpleIntroduceMock() {
    testClass.exampleMethod(1, null, null);
  }

  void context() {
    testClass.context(1, null);
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