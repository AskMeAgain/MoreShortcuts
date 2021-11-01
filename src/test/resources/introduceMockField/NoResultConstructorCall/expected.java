package IntroduceMockFieldTest.Abc;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;
import org.mockito.Mock;

class IntroduceMockFieldExample {
    @Mock
    String string1;
    @Mock
    String string2;

    void example() {
        new TestClass(string1, string2);
    }
}