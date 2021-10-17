package IntroduceMockFieldTest.Abc;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;
import org.mockito.Mockito;

class IntroduceMockVariableExample {

    private final TestClass testClass = new TestClass();

    void example() {
        var string = Mockito.mock(String.class);
        var integer = Mockito.mock(Integer.class);
        testClass.nested(1, "abc")
                .nested(1, "abc")
                .nested(integer, string);
    }
}