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

  private static final String ACTION = "io.github.askmeagain.more.shortcuts.introducemock.SmartIntroduceAction";

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
      "result-constructor-mock-field,0,false,false,true",
      "result-constructor-mock-field-private,0,false,true,true",
      "result-constructor-mock-var,1,false,false,true",
      "result-constructor-mock-var-withtype,1,false,false,false",
      "result-constructor-mock-var-static,1,true,false,true",
      "result-constructor-parameter,2,false,false,true",
      "result-constructor-spy-field,3,false,false,true",
      "result-constructor-spy-field-private,3,false,true,true",
      "result-constructor-spy-variable,4,false,false,true",
  })
  void testConstructorAction(String resultFile, Integer actionIndex, boolean staticImport, boolean privateField, boolean preferVar) {
    myFixture.configureByFiles(
        "src/test/resources/smartintroduce/input-constructor.java",
        "src/test/java/io/github/askmeagain/more/shortcuts/introducemock/entities/SmartIntroduceTestClass.java"
    );

    var state = PersistenceManagementService.getInstance().getState();

    state.setStaticImports(staticImport);
    state.setIntroduceMockFieldPrivateField(privateField);
    state.setPreferVar(preferVar);

    SmartIntroduceListDisplayAction.testIndex = actionIndex;

    myFixture.performEditorAction(ACTION);

    myFixture.checkResultByFile("src/test/resources/smartintroduce/" + resultFile + ".java", true);
  }

  @ParameterizedTest
  @CsvSource({
      "result-method-mock-field,0,false,false,true",
      "result-method-mock-field-private,0,false,true,true",
      "result-method-mock-var,1,false,false,true",
      "result-method-mock-var-withtype,1,false,false,false",
      "result-method-mock-var-static,1,true,false,true",
      "result-method-parameter,2,false,false,true",
      "result-method-spy-field,3,false,false,true",
      "result-method-spy-field-private,3,false,true,true",
      "result-method-spy-variable,4,false,false,true",
  })
  void testMethodAction(String resultFile, Integer actionIndex, boolean staticImport, boolean privateField, boolean preferVar) {
    myFixture.configureByFiles(
        "src/test/resources/smartintroduce/input-method.java",
        "src/test/java/io/github/askmeagain/more/shortcuts/introducemock/entities/SmartIntroduceTestClass.java"
    );

    var state = PersistenceManagementService.getInstance().getState();

    state.setStaticImports(staticImport);
    state.setIntroduceMockFieldPrivateField(privateField);
    state.setPreferVar(preferVar);

    SmartIntroduceListDisplayAction.testIndex = actionIndex;

    myFixture.performEditorAction(ACTION);

    myFixture.checkResultByFile("src/test/resources/smartintroduce/" + resultFile + ".java", true);
  }
}
