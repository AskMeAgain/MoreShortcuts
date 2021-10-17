package ask.me.again.shortcut.additions.introducemock;

import ask.me.again.shortcut.additions.introducemock.utils.SimpleRefactoringTestBase;
import org.junit.jupiter.api.DynamicTest;
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
//        "ConstructorMethodCall",
//        "VariableMethodCall",
//        "FieldMethodCall",
//        "StaticMethodCall1",
//        "StaticMethodCall2",
//        "NonNullParameter1",
//        "NonNullParameter2",
//        "ConstructorNoResultMethodCall",
//        "FieldNoResultMethodCall",
//        "BuilderVariableMethodCall",
//        "BuilderVariableNoResultMethodCall",
        "BuilderFieldNoResultMethodCall",
        "BuilderFieldMethodCall"
    );
  }
}
