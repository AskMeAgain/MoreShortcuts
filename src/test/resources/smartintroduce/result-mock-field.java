package io.github.askmeagain.more.shortcuts.introducemock;

import org.mockito.Mock;
import io.github.askmeagain.more.shortcuts.introducemock.SmartIntroduceTestClass;

public class Abc {

    @Mock
    private String abc;
    @Mock
    private String def;
    @Mock
    private Integer abc33;

    void test() {
        var test = 1;
        new SmartIntroduceTestClass(<caret>abc, def, abc33);
    }
}
