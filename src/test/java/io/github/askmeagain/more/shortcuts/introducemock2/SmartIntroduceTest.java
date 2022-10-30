package io.github.askmeagain.more.shortcuts.introducemock2;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class SmartIntroduceTest extends SmartIntroduceTestBase {

  private static final String ACTION = "io.github.askmeagain.more.shortcuts.introducemock2.SmartIntroduceVariableAction";

  @ParameterizedTest
  @CsvSource({"result-mock-field,0", "result-mock-var,1", "result-parameter,2"})
  void testConstructorAction(String resultFile, Integer actionIndex) {
    myFixture.configureByFiles(
        "src/test/resources/smartintroduce/input-constructor.java",
        "src/test/java/io/github/askmeagain/more/shortcuts/introducemock2/SmartIntroduceTestClass.java"
    );

    SmartIntroduceListDisplayAction.testIndex = actionIndex;

    myFixture.performEditorAction(ACTION);

    myFixture.checkResultByFile("src/test/resources/smartintroduce/" + resultFile + ".java", true);
  }
}
