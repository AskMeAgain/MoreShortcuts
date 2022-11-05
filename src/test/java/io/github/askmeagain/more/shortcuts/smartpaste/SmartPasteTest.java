package io.github.askmeagain.more.shortcuts.smartpaste;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInfo;

import static org.assertj.core.api.Assertions.assertThat;

public class SmartPasteTest extends AbstractSmartBaseTestBase {

  @Override
  public String getAction() {
    return "io.github.askmeagain.more.shortcuts.smartpaste.SmartPasteAction";
  }

  @TestFactory
  public Iterable<DynamicTest> smartPaste(TestInfo testInfo) {
    return dynamicTestsWithIterable(testInfo,
        "bracketInsertion", "\"def\"",
        "bracketInsertion2", "if(true){",
        "bracketInsertion3", "coolMethod(",
        "bracketInsertion4", "otherMethod(",
        "bracketInsertion5", "Optional<",
        "methodOrigin", "coolMethod(",
        "methodTarget", "\"def\"",
        "htmlInsertion", "<test>",
        "htmlInsertion2", "<test abc=\"attribute\">"
    );
  }
}
