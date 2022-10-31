package io.github.askmeagain.more.shortcuts.introducemock;

import io.github.askmeagain.more.shortcuts.introducemock.entities.SmartIntroduceTestClass;

public class Abc {

  void ignoreThisMethod() {
    //yes
  }

  void test() {
    var test = 1;
    new SmartIntroduceTestClass(<caret>null, method22("KEEP THIS"), null);
  }

  private String method22(String asddasd){
    return null;
  }
}
