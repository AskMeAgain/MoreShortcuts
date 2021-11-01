package IntroduceMockFieldTest.Abc;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;
import org.mockito.Mockito;

class IntroduceMockVariableExample {
    void example() {
        var testClass = new TestClass();
        var integer = Mockito.mock(Integer.class);
        var string = Mockito.mock(String.class);
        var result = testClass
                .nested(1, "abc")
                .nested(1, "abc")
                .nested(integer, string);
    }
}