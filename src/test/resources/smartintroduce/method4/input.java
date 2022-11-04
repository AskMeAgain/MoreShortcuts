package io.github.askmeagain.more.shortcuts.introducemock;

import io.github.askmeagain.more.shortcuts.introducemock.entities.SmartIntroduceTestClass;

public class Abc {

  void ignoreThisMethod() {
    //yes
  }

  void test(String def, Integer anotherParam) {
    var test = 1;
    var anotherThing = new SmartIntroduceTestClass().method(<caret>null, null, "KEEP THIS");
  }
}
