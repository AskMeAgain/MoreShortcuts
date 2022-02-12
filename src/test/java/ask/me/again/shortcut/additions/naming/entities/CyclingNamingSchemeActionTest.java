package ask.me.again.shortcut.additions.naming.entities;

import ask.me.again.shortcut.additions.introducemock.utils.SimpleRefactoringTestBase;
import ask.me.again.shortcut.additions.naming.NamingSchemeAction;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInfo;

public class CyclingNamingSchemeActionTest extends SimpleRefactoringTestBase {

  @Override
  public String getAction() {
    return "ask.me.again.shortcut.additions.naming.NamingSchemeAction";
  }

  @TestFactory
  public Iterable<DynamicTest> cyclingNamingScheme(TestInfo testInfo) {
    NamingSchemeAction.index = -2;
    return dynamicTestsWithIterable(testInfo,
        "SnakeCase",
        "CamelCase",
        "DoenerCase",
        "PascalCase",
        "DotCase"
    );
  }
}
