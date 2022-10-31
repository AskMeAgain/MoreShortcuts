package io.github.askmeagain.more.shortcuts.introducemock;

import io.github.askmeagain.more.shortcuts.introducemock.entities.SmartIntroduceTestClass;

public class Abc {

    void ignoreThisMethod() {
        //yes
    }

    void test(String def, Integer anotherParam, String abc, Optional<Integer> abc33) {
        var test = 1;
        new SmartIntroduceTestClass().method(<caret>abc, def, "KEEP THIS");
    }
}
