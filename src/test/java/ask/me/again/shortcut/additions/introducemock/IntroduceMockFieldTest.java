package ask.me.again.shortcut.additions.introducemock;

import ask.me.again.shortcut.additions.introducemock.utils.SimpleRefactoringTestBase;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInfo;

public class IntroduceMockFieldTest extends SimpleRefactoringTestBase {

  @Override
  public String getAction(){
    return "ask.me.again.shortcut.additions.introducemock.field";
  }

  @TestFactory
  public Iterable<DynamicTest> introduceMockField(TestInfo testInfo) throws Exception {
    return dynamicTestsWithIterable(testInfo,
        "ConstructorMethodCall",
        "VariableMethodCall",
        "FieldMethodCall",
        "StaticMethodCall1",
        "StaticMethodCall2"
    );
  }

  @Test
  public void test() throws Exception {
    setUp();
    myFixture.configureByFiles(
        String.format("/src/test/resources/%s/%s/input.java", "introduceMockField", "Context1"),
        "/src/test/java" + MODULE + "/entities/TestClass.java",
        "/src/test/resources/mockito.java",
        "/src/test/resources/mock.java"
    );

    myFixture.performEditorAction(getAction());
    myFixture.type("<down><enter>");
    myFixture.checkResultByFile(String.format("/src/test/resources/%s/%s/expected.java", "introduceMockField", "Context1"));
  }
}
