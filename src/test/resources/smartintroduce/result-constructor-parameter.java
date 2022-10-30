package io.github.askmeagain.more.shortcuts.introducemock;

import io.github.askmeagain.more.shortcuts.introducemock.entities.SmartIntroduceTestClass;

public class Abc {

    void ignoreThisMethod() {
        //yes
    }

    void test(String abc, String def, Optional<Integer> abc33) {
        var test = 1;
        new SmartIntroduceTestClass(<caret>abc, def, abc33);
    }
}
