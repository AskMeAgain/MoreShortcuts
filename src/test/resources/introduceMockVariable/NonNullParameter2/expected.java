package IntroduceMockFieldTest.Abc;

import io.github.askmeagain.more.shortcuts.introducemock.entities.TestClass;
import org.mockito.Mockito;

class IntroduceMockVariableExample {
    void example() {
        var testClass = new TestClass();
        var string = Mockito.mock(String.class);

        var result = testClass.nested(1, string);
    }
}