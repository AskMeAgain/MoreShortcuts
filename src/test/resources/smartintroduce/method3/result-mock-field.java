package io.github.askmeagain.more.shortcuts.introducemock;

import org.mockito.Mock;
import io.github.askmeagain.more.shortcuts.introducemock.entities.SmartIntroduceTestClass;

public class Abc {

    @Mock
    String abc;
    @Mock
    String def;

    void ignoreThisMethod() {
        //yes
    }

    void test(String def, Integer anotherParam) {
        var test = 1;
        new SmartIntroduceTestClass().method(<caret>abc, def, "KEEP THIS");
    }
}
