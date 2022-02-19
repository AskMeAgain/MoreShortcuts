package ask.me.again.shortcut.additions.naming;

import ask.me.again.shortcut.additions.introducemock.utils.SimpleRefactoringTestBase;
import ask.me.again.shortcut.additions.naming.impl.NamingSchemeAction;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInfo;

public class CyclingNamingSchemeActionTest extends SimpleRefactoringTestBase {

  @Override
  public String getAction() {
    return "ask.me.again.shortcut.additions.naming.impl.NamingSchemeAction";
  }

  @TestFactory
  public Iterable<DynamicTest> cyclingNamingScheme(TestInfo testInfo) {
    NamingSchemeAction.INDEX = -2;
    return dynamicTestsWithIterable(testInfo,
        "SnakeCase", "",
        "CamelCase", "",
        "DoenerCase", "",
        "PascalCase", "",
        "DotCase", ""
    );
  }
}
