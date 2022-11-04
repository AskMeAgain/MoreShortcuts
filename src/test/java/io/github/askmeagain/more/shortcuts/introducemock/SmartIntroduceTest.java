package io.github.askmeagain.more.shortcuts.introducemock;

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import io.github.askmeagain.more.shortcuts.settings.PersistenceManagementService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

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

  private static Stream<Arguments> testCases() {
    return Stream.of(
        Arguments.of("result-mock-field", 0, false, false, true),
        Arguments.of("result-mock-field-private", 0, false, true, true),
        Arguments.of("result-mock-var", 1, false, false, true),
        Arguments.of("result-mock-var-withtype", 1, false, false, false),
        Arguments.of("result-mock-var-static", 1, true, false, true),
        Arguments.of("result-parameter", 2, false, false, true),
        Arguments.of("result-spy-field", 3, false, false, true),
        Arguments.of("result-spy-field-private", 3, false, true, true),
        Arguments.of("result-spy-variable", 4, false, false, true)
    );
  }

  @ParameterizedTest
  @MethodSource("testCases")
  void testSmartIntroduceConstructor(String resultFile, Integer actionIndex, boolean staticImport, boolean privateField, boolean preferVar) {
    runTestCase("constructor", resultFile, actionIndex, staticImport, privateField, preferVar);
  }

  @ParameterizedTest
  @MethodSource("testCases")
  void testSmartIntroduceConstructor2(String resultFile, Integer actionIndex, boolean staticImport, boolean privateField, boolean preferVar) {
    runTestCase("constructor2", resultFile, actionIndex, staticImport, privateField, preferVar);
  }

  @ParameterizedTest
  @MethodSource("testCases")
  void testSmartIntroduceMethod(String resultFile, Integer actionIndex, boolean staticImport, boolean privateField, boolean preferVar) {
    runTestCase("method", resultFile, actionIndex, staticImport, privateField, preferVar);
  }

  @ParameterizedTest
  @MethodSource("testCases")
  void testSmartIntroduceMethod2(String resultFile, Integer actionIndex, boolean staticImport, boolean privateField, boolean preferVar) {
    runTestCase("method2", resultFile, actionIndex, staticImport, privateField, preferVar);
  }

  @ParameterizedTest
  @MethodSource("testCases")
  void testSmartIntroduceMethod3(String resultFile, Integer actionIndex, boolean staticImport, boolean privateField, boolean preferVar) {
    runTestCase("method3", resultFile, actionIndex, staticImport, privateField, preferVar);
  }

  @ParameterizedTest
  @MethodSource("testCases")
  void testSmartIntroduceMethod4(String resultFile, Integer actionIndex, boolean staticImport, boolean privateField, boolean preferVar) {
    runTestCase("method4", resultFile, actionIndex, staticImport, privateField, preferVar);
  }

  @ParameterizedTest
  @MethodSource("testCases")
  void testSmartIntroduceConstructor3(String resultFile, Integer actionIndex, boolean staticImport, boolean privateField, boolean preferVar) {
    runTestCase("constructor3", resultFile, actionIndex, staticImport, privateField, preferVar);
  }

  @ParameterizedTest
  @MethodSource("testCases")
  void testSmartIntroduceConstructor4(String resultFile, Integer actionIndex, boolean staticImport, boolean privateField, boolean preferVar) {
    runTestCase("constructor4", resultFile, actionIndex, staticImport, privateField, preferVar);
  }

  private void runTestCase(String testCase, String resultFile, Integer actionIndex, boolean staticImport, boolean privateField, boolean preferVar) {
    myFixture.configureByFiles(
        String.format("src/test/resources/smartintroduce/%s/input.java", testCase),
        "src/test/java/io/github/askmeagain/more/shortcuts/introducemock/entities/SmartIntroduceTestClass.java"
    );

    var state = PersistenceManagementService.getInstance().getState();

    state.setStaticImports(staticImport);
    state.setIntroduceMockFieldPrivateField(privateField);
    state.setPreferVar(preferVar);

    SmartIntroduceListDisplayAction.testIndex = actionIndex;

    myFixture.performEditorAction(ACTION);

    myFixture.checkResultByFile("src/test/resources/smartintroduce/" + testCase + "/" + resultFile + ".java", true);
  }
}
