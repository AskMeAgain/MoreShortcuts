package ask.me.again.shortcut.additions.introducemock.test;


public class TestCases {

  public static void testAsd(int i, String isa) {

  }

  public TestCases() {
  }

  public void asd(Integer a, String d, Integer e) {

  }

  public TestCases nested(Integer a, String b) {
    return this;
  }

  public TestCases(TestCases a, TestCases b, Integer c) {
  }

  public TestCases(Integer a, TestCases b, Integer c) {

  }

  public static void main(String[] args) {

    var testClass = new TestCases(1, null, null);

    testClass.asd(null, null, null);

    testClass.nested(1, "")
        .nested(null, null);

    TestCases.testAsd(0, null);

  }
}


