package io.github.askmeagain.more.shortcuts.introducemock;

import org.mockito.Spy;
import io.github.askmeagain.more.shortcuts.introducemock.entities.SmartIntroduceTestClass;

public class Abc {

    @Spy
    private String abc;
    @Spy
    private String def;
    @Spy
    private Optional<Integer> abc33;

    void test() {
        var test = 1;
        new SmartIntroduceTestClass().method(<caret>abc, def, abc33);
    }
}
