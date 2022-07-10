package IntroduceMockFieldTest.Abc;

import io.github.askmeagain.more.shortcuts.introducemock.entities.TestClass;
import org.mockito.Mockito;

class IntroduceMockVariableExample {
    void example() {
        var string = Mockito.mock(String.class);

        var result = TestClass.staticMethod(null, string);
    }
}