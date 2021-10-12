package ask.me.again.shortcut.additions.introducemock.introducemockvariable;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;
import org.mockito.Mockito;

class IntroduceMockVariableExample {

  void example() {
    var string = Mockito.mock(String.class);
    var integer = Mockito.mock(Integer.class);
    var result = TestClass.staticMethod(integer, string);
  }

}
