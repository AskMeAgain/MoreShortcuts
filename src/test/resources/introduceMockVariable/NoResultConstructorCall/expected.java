package IntroduceMockFieldTest.Abc;

import io.github.askmeagain.more.shortcuts.introducemock.entities.TestClass;
import org.mockito.Mockito;

class IntroduceMockFieldExample {

    void example() {
        var string1 = Mockito.mock(String.class);
        var string2 = Mockito.mock(String.class);

        new TestClass(string1, string2);
    }
}