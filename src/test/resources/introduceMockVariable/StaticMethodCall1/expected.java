package IntroduceMockFieldTest.Abc;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;
import org.mockito.Mockito;

class IntroduceMockVariableExample {
    void example() {
        var string = Mockito.mock(String.class);

        var result = TestClass.staticMethod(null, string);
    }
}