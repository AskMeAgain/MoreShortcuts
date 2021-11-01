package ask.me.again.shortcut.additions.introducemock;

import ask.me.again.shortcut.additions.introducemock.utils.SimpleRefactoringTestBase;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInfo;

public class IntroduceMockVariableTest extends SimpleRefactoringTestBase {

  @Override
  public String getAction() {
    return "ask.me.again.shortcut.additions.introducemock.variable";
  }

  @TestFactory
  public Iterable<DynamicTest> introduceMockVariable(TestInfo testInfo) {
    return dynamicTestsWithIterable(testInfo,
        "ConstructorMethodCall",
        "ConstructorNoResultMethodCall",
        "VariableMethodCall",
        "FieldMethodCall",
        "StaticMethodCall1",
        "StaticMethodCall2",
        "NonNullParameter1",
        "NonNullParameter2",
        "FieldNoResultMethodCall",
        "BuilderVariableMethodCall",
        "BuilderVariableNoResultMethodCall",
        "BuilderFieldNoResultMethodCall",
        "BuilderFieldMethodCall",
        "ConstructorCall",
        "SameParameterName",
        "NoResultConstructorCall"
    );
  }
}
