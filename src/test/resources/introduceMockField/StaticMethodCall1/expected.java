package IntroduceMockFieldTest.Abc;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;
import org.junit.Test;
import org.mockito.Mock;

class IntroduceMockFieldExample {
    @Mock
    String string;

    void example() {

        var result = TestClass.staticMethod(null, string);
    }
}
