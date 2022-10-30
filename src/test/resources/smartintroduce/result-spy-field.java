package io.github.askmeagain.more.shortcuts.introducemock;

import org.mockito.Spy;
import io.github.askmeagain.more.shortcuts.introducemock.SmartIntroduceTestClass;

public class Abc {

    @Spy
    private String abc;
    @Spy
    private String def;
    @Spy
    private Integer abc33;

    void test() {
        var test = 1;
        new SmartIntroduceTestClass(<caret>abc, def, abc33);
    }
}
