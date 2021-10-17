package IntroduceMockFieldTest.Abc;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;
import org.junit.Test;
import org.mockito.Mockito;

class IntroduceMockVariableExample {
    void example() {
        var string = Mockito.mock(String.class);
        var integer = Mockito.mock(Integer.class);
        var result = TestClass.staticMethod2(integer, string);
    }
}