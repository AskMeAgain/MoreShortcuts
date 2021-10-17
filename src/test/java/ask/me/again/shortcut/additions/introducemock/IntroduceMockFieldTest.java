package ask.me.again.shortcut.additions.introducemock;

import ask.me.again.shortcut.additions.introducemock.actions.IntroduceMockToField;
import ask.me.again.shortcut.additions.introducemock.utils.SimpleRefactoringTestBase;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInfo;

public class IntroduceMockFieldTest extends SimpleRefactoringTestBase {

  @Override
  public IntroduceMockToField getAction(){
    return new IntroduceMockToField();
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
  public void test(){

  }
}
