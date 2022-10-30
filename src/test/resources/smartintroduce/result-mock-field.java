package io.github.askmeagain.more.shortcuts.introducemock2;

import io.github.askmeagain.more.shortcuts.introducemock2.SmartIntroduceTestClass;

public class Abc {

    @Mock
    private String abc;
    @Mock
    private String def;
    @Mock
    private Integer abc33;

    void test() {
        var test = 1;
        new SmartIntroduceTestClass(<caret>abc, def, abc33);
    }
}
