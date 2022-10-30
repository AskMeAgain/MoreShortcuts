package io.github.askmeagain.more.shortcuts.introducemock;

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import io.github.askmeagain.more.shortcuts.introducemock.SmartIntroduceListDisplayAction;
import io.github.askmeagain.more.shortcuts.settings.MoreShortcutState;
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
      "result-mock-field,0,false,false",
      "result-mock-field-private,0,false,true",
      "result-mock-var,1,false,false",
      "result-mock-var-static,1,true,false",
      "result-parameter,2,false,false",
      "result-spy-field,3,false,false",
      "result-spy-field-private,3,false,true",
      "result-spy-variable,4,false,false",
  })
  void testConstructorAction(String resultFile, Integer actionIndex, boolean staticImport, boolean privateField) {
    myFixture.configureByFiles(
        "src/test/resources/smartintroduce/input-constructor.java",
        "src/test/java/io/github/askmeagain/more/shortcuts/introducemock/entities/SmartIntroduceTestClass.java"
    );

    var state = PersistenceManagementService.getInstance().getState();

    state.setStaticImports(staticImport);
    state.setIntroduceMockFieldPrivateField(privateField);

    SmartIntroduceListDisplayAction.testIndex = actionIndex;

    myFixture.performEditorAction(ACTION);

    myFixture.checkResultByFile("src/test/resources/smartintroduce/" + resultFile + ".java", true);
  }
}
