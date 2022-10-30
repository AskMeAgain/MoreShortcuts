package io.github.askmeagain.more.shortcuts.smartpaste;

import io.github.askmeagain.more.shortcuts.introducemock.SimpleRefactoringTestBase;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInfo;

import static org.assertj.core.api.Assertions.assertThat;

public class SmartPasteTest extends SimpleRefactoringTestBase {

  @Override
  public String getAction() {
    return "io.github.askmeagain.more.shortcuts.smartpaste.SmartPasteAction";
  }

  @TestFactory
  public Iterable<DynamicTest> smartPaste(TestInfo testInfo) {
    return dynamicTestsWithIterable(testInfo,
        "bracketInsertion", "\"def\"",
        "bracketInsertion2", "if(true){",
        "methodOrigin", "coolMethod(",
        "methodTarget", "\"def\"",
        "bracketInsertion3", "coolMethod(",
        "bracketInsertion4", "otherMethod(",
        "htmlInsertion", "<test>"
    );
  }
}
