package ask.me.again.shortcut.additions.introducemock.introducemockfield;

import io.github.askmeagain.more.shortcuts.introducemock.entities.TestClass;
import org.mockito.Mock;

class IntroduceMockFieldExample {
    @Mock
    Integer integer;
    @Mock
    String string;

    void example() {
        var result = new TestClass().nested(integer, string);
    }
}
