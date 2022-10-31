package io.github.askmeagain.more.shortcuts.introducemock;

import org.mockito.Mockito;
import io.github.askmeagain.more.shortcuts.introducemock.entities.SmartIntroduceTestClass;

public class Abc {

    void ignoreThisMethod() {
        //yes
    }

    void test() {
        var test = 1;

        var abc = Mockito.spy(String.class);
        var abc33 = Mockito.spy(Optional.class);

        new SmartIntroduceTestClass(<caret>abc, method22("KEEP THIS"), abc33);
    }

    private String method22(String asddasd) {
        return null;
    }
}
