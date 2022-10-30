package io.github.askmeagain.more.shortcuts.introducemock2;

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;

public class SmartIntroduceTest extends LightJavaCodeInsightFixtureTestCase {

  private static final String ACTION = "io.github.askmeagain.more.shortcuts.introducemock2.SmartIntroduceVariableAction";

  @Override
  public final String getTestDataPath() {
    return System.getProperty("user.dir");
  }

  @SneakyThrows
  @BeforeEach
  void setup() {
    super.setUp();
  }

  @SneakyThrows
  @AfterEach
  void reset() {
    super.tearDown();
  }

  @ParameterizedTest
  @CsvSource({
      "result-mock-field,0",
      "result-mock-var,1",
      "result-parameter,2",
      "result-spy-field,3",
      "result-spy-variable,4",
  })
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
