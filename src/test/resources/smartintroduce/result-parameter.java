package io.github.askmeagain.more.shortcuts.introducemock2;

import io.github.askmeagain.more.shortcuts.introducemock2.SmartIntroduceTestClass;

public class Abc {

    void test(String abc, String def, Integer abc33) {
        var test = 1;
        new SmartIntroduceTestClass(<caret>abc, def, abc33);
    }
}