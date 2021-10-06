package ask.me.again.shortcut.additions.introducemock.test;


public class TestClass {

  public TestClass() {
  }

  public void asd(Integer a, String d, Integer e) {

  }

  public TestClass nested(Integer a, String b) {
    return this;
  }

  public TestClass(TestClass a, TestClass b, Integer c) {

  }

  public TestClass(Integer a, TestClass b, Integer c) {

  }

  public static void main(String[] args) {

    var mock = Mockito.mock(Class.class);

    var testClass = new TestClass(1, null, null);

    testClass.asd(null, null, null);

    testClass.nested(1, "")
        .nested(null, null);


  }


}
