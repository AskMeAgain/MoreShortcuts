package io.github.askmeagain.more.shortcuts.introducemock;

import static org.mockito.mock;

import io.github.askmeagain.more.shortcuts.introducemock.entities.SmartIntroduceTestClass;

public class Abc {

    void test() {
        var test = 1;

        var abc = mock(String.class);
        var def = mock(String.class);
        var abc33 = mock(Optional.class);

        new SmartIntroduceTestClass().method(<caret>abc, def, abc33);
    }
}
