package io.github.askmeagain.more.shortcuts.introducemock;

import io.github.askmeagain.more.shortcuts.introducemock.entities.SmartIntroduceTestClass;

public class Abc {

  void ignoreThisMethod(){
    //yes
  }

  void test(){
    var test = 1;
    new SmartIntroduceTestClass(<caret>);
  }
}
