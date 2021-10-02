package ask.me.again.shortcut.additions.introducemock.helpers;


public class TestClass {

  public TestClass() {
  }

  public void asd(Integer a, String d, Integer e) {

  }

  public TestClass(TestClass a, TestClass b, Integer c) {

  }

  public static void main(String[] args) {

    var mock = Mockito.mock(Class.class);

    var testClass = new TestClass(null, null, null);

    testClass.asd(null, null, null);
  }


}
