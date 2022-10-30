package io.github.askmeagain.more.shortcuts.introducemock;

import io.github.askmeagain.more.shortcuts.introducemock.entities.SmartIntroduceTestClass;

public class Abc {

    void test() {
        var test = 1;

        var abc = Mockito.spy(String.class);
        var def = Mockito.spy(String.class);
        var abc33 = Mockito.spy(Optional.class);

        new SmartIntroduceTestClass(<caret>abc, def, abc33);
    }
}
