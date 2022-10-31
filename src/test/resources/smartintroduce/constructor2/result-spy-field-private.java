package io.github.askmeagain.more.shortcuts.introducemock;

import org.mockito.Spy;
import io.github.askmeagain.more.shortcuts.introducemock.entities.SmartIntroduceTestClass;

public class Abc {

    @Spy
    private String abc;
    @Spy
    private Optional<Integer> abc33;

    void ignoreThisMethod() {
        //yes
    }

    void test() {
        var test = 1;
        new SmartIntroduceTestClass(<caret>abc, "KEEP THIS", abc33);
    }
}
