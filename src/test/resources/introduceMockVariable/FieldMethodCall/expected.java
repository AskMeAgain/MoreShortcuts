package IntroduceMockFieldTest.Abc;

import io.github.askmeagain.more.shortcuts.introducemock.entities.TestClass;
import org.mockito.Mockito;

class IntroduceMockVariableExample {

    private TestClass testClass = new TestClass();

    void example() {
        var integer = Mockito.mock(Integer.class);
        var string = Mockito.mock(String.class);

        var result = testClass.nested(integer, string);
    }
}