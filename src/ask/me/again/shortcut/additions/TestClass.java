package ask.me.again.shortcut.additions;


public class TestClass {

  public TestClass() {
  }

  public TestClass(TestClass a, Integer b) {

  }

  public static void main(String[] args) {

    var integer = Mockito.mock(Integer.class);
    var testClass = Mockito.mock(TestClass.class);
    var s = new TestClass(testClass, integer);
  }


}
