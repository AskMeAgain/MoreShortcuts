package io.github.askmeagain.more.shortcuts.introducemock;

import org.mockito.Spy;
import io.github.askmeagain.more.shortcuts.introducemock.entities.SmartIntroduceTestClass;

public class Abc {

    @Spy
    String abc;
    @Spy
    String def;

    void ignoreThisMethod() {
        //yes
    }

    void test(String def, Integer anotherParam) {
        var test = 1;
        new SmartIntroduceTestClass().method(<caret>abc, def, "KEEP THIS");
    }
}
