package io.github.askmeagain.more.shortcuts.introducemock;

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import io.github.askmeagain.more.shortcuts.introducemock.SmartIntroduceListDisplayAction;
import io.github.askmeagain.more.shortcuts.settings.PersistenceManagementService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class SmartIntroduceTest extends LightJavaCodeInsightFixtureTestCase {

  private static final String ACTION = "io.github.askmeagain.more.shortcuts.introducemock.SmartIntroduceVariableAction";

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
      "result-mock-field,0,false",
      "result-mock-var,1,false",
      "result-mock-var-static,1,true",
      "result-parameter,2,false",
      "result-spy-field,3,false",
      "result-spy-variable,4,false",
  })
  void testConstructorAction(String resultFile, Integer actionIndex, boolean staticImport) {
    myFixture.configureByFiles(
        "src/test/resources/smartintroduce/input-constructor.java",
        "src/test/java/io/github/askmeagain/more/shortcuts/introducemock/entities/SmartIntroduceTestClass.java"
    );

    PersistenceManagementService.getInstance().getState().setStaticImports(staticImport);

    SmartIntroduceListDisplayAction.testIndex = actionIndex;

    myFixture.performEditorAction(ACTION);

    myFixture.checkResultByFile("src/test/resources/smartintroduce/" + resultFile + ".java", true);
  }
}
