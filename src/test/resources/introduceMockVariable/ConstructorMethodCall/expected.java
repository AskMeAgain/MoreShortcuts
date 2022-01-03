package ask.me.again.shortcut.additions.introducemock.introducemockfield;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;
import org.mockito.Mockito;

class IntroduceMockVariableExample {
    void example() {
        var integer = Mockito.mock(Integer.class);
        var string = Mockito.mock(String.class);

        var result = new TestClass().nested(integer, string);
    }
}