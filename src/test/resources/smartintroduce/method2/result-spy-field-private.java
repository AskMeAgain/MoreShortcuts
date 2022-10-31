package io.github.askmeagain.more.shortcuts.introducemock;

import org.mockito.Spy;
import io.github.askmeagain.more.shortcuts.introducemock.entities.SmartIntroduceTestClass;

public class Abc {

    @Spy
    private String abc;
    @Spy
    private String def;

    void ignoreThisMethod() {
        //yes
    }

    void test() {
        var test = 1;
        new SmartIntroduceTestClass().method(<caret>abc, def, "KEEP THIS");
    }
}
