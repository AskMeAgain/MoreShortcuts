package IntroduceMockFieldTest.Abc;

import io.github.askmeagain.more.shortcuts.introducemock.entities.TestClass;
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