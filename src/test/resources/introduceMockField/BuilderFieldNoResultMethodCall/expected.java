package IntroduceMockFieldTest.Abc;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;
import org.mockito.Mock;

class IntroduceMockFieldExample {
    @Mock
    Integer integer;
    @Mock
    String string;

    private final TestClass testClass = new TestClass();

    void example() {
        testClass.nested(1, "abc")
                .nested(1, "abc")
                .nested(integer, string);
    }
}