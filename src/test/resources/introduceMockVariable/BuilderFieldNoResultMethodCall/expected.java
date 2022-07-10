package IntroduceMockFieldTest.Abc;

import io.github.askmeagain.more.shortcuts.introducemock.entities.TestClass;
import org.mockito.Mockito;

class IntroduceMockVariableExample {

    private final TestClass testClass = new TestClass();

    void example() {
        var integer = Mockito.mock(Integer.class);
        var string = Mockito.mock(String.class);

        testClass.nested(1, "abc")
                .nested(1, "abc")
                .nested(integer, string);
    }
}