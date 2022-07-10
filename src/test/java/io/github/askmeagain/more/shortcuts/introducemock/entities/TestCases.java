package io.github.askmeagain.more.shortcuts.introducemock.entities;

import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

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
    var string = Mockito.mock(String.class);
    Mockito.when(string.compareTo(any())).thenReturn(null);

    var integer = Mockito.mock(Integer.class);
    testClass.nested(1, "")
        .nested(1, "")
        .nested(1, "")
        .nested(1, "")
        .nested(integer, string);
  }

  void builderClassAssignment() {
    testClass = testClass.nested(1, "")
        .nested(1, "")
        .nested(1, "")
        .nested(1, "")
        .nested(null, null);
  }

}