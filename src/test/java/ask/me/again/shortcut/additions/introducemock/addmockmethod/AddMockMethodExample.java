package ask.me.again.shortcut.additions.introducemock.addmockmethod;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;
import org.mockito.Mockito;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;

public class AddMockMethodExample {

  public void example(){
    var interestingMock = Mockito.mock(TestClass.class);
    Mockito.when(interestingMock.exampleMethod(anyInt(), any(), anyDouble())).thenReturn(null);
  }
}
