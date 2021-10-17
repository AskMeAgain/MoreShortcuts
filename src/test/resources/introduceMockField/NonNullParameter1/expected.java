package IntroduceMockFieldTest.Abc;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;
import org.mockito.Mock;

class IntroduceMockFieldExample {
    @Mock
    Integer integer;

    void example() {
        var testClass = new TestClass();
        var result = testClass.nested(integer, "Not null");
    }
}