package io.github.askmeagain.more.shortcuts.smartintroduce;

import org.junit.jupiter.api.Test;

public class FirstSmartIntroduceTest extends SmartIntroduceTestBase {

  @Test
  void abc() {
    var action = "io.github.askmeagain.more.shortcuts.introducemock2.SmartIntroduceVariableAction";
    myFixture.configureByFiles(
        "src/test/resources/smartintroduce/smartintroduce.java",
        "src/test/java/io/github/askmeagain/more/shortcuts/smartintroduce/SmartIntroduceTestClass.java"
    );
    myFixture.performEditorAction(action);

    myFixture.checkResultByFile("src/test/resources/smartintroduce/result.java", true);

  }
}
