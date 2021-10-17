package IntroduceMockFieldTest.Abc;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;
import org.mockito.Mockito;

class IntroduceMockVariableExample {
    void example() {
        var testClass = new TestClass();
        var string = Mockito.mock(String.class);
        var integer = Mockito.mock(Integer.class);
        var result = testClass.nested(integer, string);
    }
}