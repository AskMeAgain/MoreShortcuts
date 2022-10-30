package io.github.askmeagain.more.shortcuts.introducemock;

import org.mockito.Mock;
import io.github.askmeagain.more.shortcuts.introducemock.entities.SmartIntroduceTestClass;

public class Abc {

    @Mock
    String abc;
    @Mock
    String def;
    @Mock
    Optional<Integer> abc33;

    void test() {
        var test = 1;
        new SmartIntroduceTestClass().method(<caret>abc, def, abc33);
    }
}
