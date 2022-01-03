package IntroduceMockFieldTest.Abc;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;
import org.mockito.Mock;
import org.mockito.Mockito;

class IntroduceMockVariableExample {
    void example() {
        var testClass = new TestClass();
        var string = Mockito.mock(String.class);

        var result = testClass.nested(1, string);
    }
}