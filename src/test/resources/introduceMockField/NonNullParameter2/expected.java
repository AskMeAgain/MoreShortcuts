package IntroduceMockFieldTest.Abc;

import io.github.askmeagain.more.shortcuts.introducemock.entities.TestClass;
import org.mockito.Mock;

class IntroduceMockFieldExample {
    @Mock
    String string;

    void example() {
        var testClass = new TestClass();
        var result = testClass.nested(1, string);
    }
}
