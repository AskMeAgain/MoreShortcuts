package io.github.askmeagain.more.shortcuts.introducemock;

import org.mockito.Mock;
import io.github.askmeagain.more.shortcuts.introducemock.entities.SmartIntroduceTestClass;

public class Abc {

    @Mock
    private String abc;
    @Mock
    private String def;

    void ignoreThisMethod() {
        //yes
    }

    void test() {
        var test = 1;
        new SmartIntroduceTestClass().method(<caret>abc, def, "KEEP THIS");
    }
}
