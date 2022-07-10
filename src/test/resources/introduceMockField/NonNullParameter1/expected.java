package IntroduceMockFieldTest.Abc;

import io.github.askmeagain.more.shortcuts.introducemock.entities.TestClass;
import org.mockito.Mock;

class IntroduceMockFieldExample {
    @Mock
    Integer integer;

    void example() {
        var testClass = new TestClass();
        var result = testClass.nested(integer, "Not null");
    }
}