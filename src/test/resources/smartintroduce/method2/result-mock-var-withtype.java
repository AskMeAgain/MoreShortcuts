package io.github.askmeagain.more.shortcuts.introducemock;

import org.mockito.Mockito;
import io.github.askmeagain.more.shortcuts.introducemock.entities.SmartIntroduceTestClass;

public class Abc {

    void ignoreThisMethod() {
        //yes
    }

    void test() {
        var test = 1;

        String abc = Mockito.mock(String.class);
        String def = Mockito.mock(String.class);

        new SmartIntroduceTestClass().method(<caret>abc, def, "KEEP THIS");
    }
}
