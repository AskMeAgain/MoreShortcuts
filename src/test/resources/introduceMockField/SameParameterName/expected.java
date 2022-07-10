package IntroduceMockFieldTest.Abc;

import io.github.askmeagain.more.shortcuts.introducemock.entities.TestClass;
import org.mockito.Mock;

class IntroduceMockFieldExample {
    @Mock
    String string1;
    @Mock
    String string2;

    void example() {
        var result = TestClass.sameParameterName(string1, string2);
    }
}
