package io.github.askmeagain.more.shortcuts.introducemock2;

import io.github.askmeagain.more.shortcuts.introducemock2.SmartIntroduceTestClass;

public class Abc {

    void test() {
        var test = 1;

        var abc = Mockito.spy(String.class);
        var def = Mockito.spy(String.class);
        var abc33 = Mockito.spy(Integer.class);

        new SmartIntroduceTestClass(<caret>abc, def, abc33);
    }
}
