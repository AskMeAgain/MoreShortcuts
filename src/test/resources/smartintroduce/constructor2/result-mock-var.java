package io.github.askmeagain.more.shortcuts.introducemock;

import org.mockito.Mockito;
import io.github.askmeagain.more.shortcuts.introducemock.entities.SmartIntroduceTestClass;

public class Abc {

    void ignoreThisMethod() {
        //yes
    }

    void test() {
        var test = 1;

        var abc = Mockito.mock(String.class);
        var def = Mockito.mock(String.class);
        var abc33 = Mockito.mock(Optional.class);

        new SmartIntroduceTestClass(<caret>abc, def, abc33);
    }
}
