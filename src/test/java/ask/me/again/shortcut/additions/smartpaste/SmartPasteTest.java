package ask.me.again.shortcut.additions.smartpaste;

import ask.me.again.shortcut.additions.introducemock.utils.SimpleRefactoringTestBase;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInfo;

public class SmartPasteTest extends SimpleRefactoringTestBase {

  @Override
  public String getAction() {
    return "ask.me.again.shortcut.additions.smartpaste.SmartPasteAction";
  }

  @TestFactory
  public Iterable<DynamicTest> smartPaste(TestInfo testInfo) {

    return dynamicTestsWithIterable(testInfo,
        "bracketInsertion","\"def\""
    );
  }
}
