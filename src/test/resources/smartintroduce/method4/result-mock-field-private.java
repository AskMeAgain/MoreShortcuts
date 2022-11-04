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

    void test(String def, Integer anotherParam) {
        var test = 1;
        var anotherThing = new SmartIntroduceTestClass().method(<caret>abc, def, "KEEP THIS");
    }
}
