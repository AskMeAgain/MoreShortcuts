package ask.me.again.shortcut.additions.introducemock.introducemockfield;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;
import org.mockito.Mock;

class IntroduceMockFieldExample {
  @Mock
  Integer integer;
  @Mock
  String string;

  void example() {
    var result = TestClass.staticMethod(integer, string);
  }
}
