package IntroduceMockFieldTest.Abc;

import io.github.askmeagain.more.shortcuts.introducemock.entities.TestClass;
import org.mockito.Mockito;

class IntroduceMockVariableExample {
    void example() {
        var integer = Mockito.mock(Integer.class);
        var string = Mockito.mock(String.class);

        var result = TestClass.staticMethod2(integer, string);
    }
}