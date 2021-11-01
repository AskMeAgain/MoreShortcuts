package IntroduceMockFieldTest.Abc;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;
import org.mockito.Mock;

class IntroduceMockVariableExample {
    @Mock
    Integer integer;
    @Mock
    String string;

    void example() {
        var result = new TestClass().context(integer, string);
    }
}
