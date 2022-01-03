package IntroduceMockFieldTest.Abc;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;
import org.junit.Test;
import org.mockito.Mockito;

class IntroduceMockFieldExample {
    void example() {
        var string1 = Mockito.mock(String.class);
        var string2 = Mockito.mock(String.class);

        var result = TestClass.sameParameterName(string1, string2);
    }
}