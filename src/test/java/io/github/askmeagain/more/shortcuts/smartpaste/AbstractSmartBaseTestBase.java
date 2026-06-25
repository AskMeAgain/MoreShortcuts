package io.github.askmeagain.more.shortcuts.smartpaste;

import com.intellij.ide.CopyPasteManagerEx;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.intellij.util.ui.TextTransferable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestInfo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractSmartBaseTestBase extends LightJavaCodeInsightFixtureTestCase {

  protected final static String MODULE = "/io/github/askmeagain/more/shortcuts/introducemock";
  private boolean registeredAction;

  public Iterable<DynamicTest> dynamicTestsWithIterable(TestInfo testInfo, String... testcases) {
    var testName = testInfo.getTestMethod().get().getName();

    var result = new ArrayList<DynamicTest>();
    for (int i = 0; i < testcases.length; i++, i++) {
      var caseName = testcases[i];
      var clipboard = testcases[i + 1];
      var test = createDynamicTest(testName, caseName, clipboard);
      result.add(test);
    }

    return result;
  }

  private DynamicTest createDynamicTest(String testClassName, String caseName, String clipboard) {
    return DynamicTest.dynamicTest(caseName, () ->
    {
      myFixture.configureByFiles(
          String.format("/src/test/resources/%s/%s/input.java", testClassName, caseName),
          "/src/test/java" + MODULE + "/entities/TestClass.java",
          "/src/test/resources/defaults/mockito.java",
          "/src/test/resources/defaults/mock.java"
      );

      CopyPasteManagerEx.getInstance().setContents(new TextTransferable(clipboard));
      myFixture.performEditorAction(getAction());

      var expectedPath = Path.of(getTestDataPath(), "src/test/resources", testClassName, caseName, "expected.java");
      var expected = Files.readString(expectedPath).replace("\r\n", "\n").stripTrailing();
      var actual = myFixture.getEditor().getDocument().getText().replace("\r\n", "\n").stripTrailing();

      assertThat(actual).isEqualTo(expected);
    });
  }

  public abstract String getAction();

  public abstract AnAction createAction();

  @BeforeEach
  void setupTest() throws Exception {
    setUp();

    var actionManager = ActionManager.getInstance();
    if (actionManager.getAction(getAction()) == null) {
      actionManager.registerAction(getAction(), createAction());
      registeredAction = true;
    }
  }

  @AfterEach
  void teardownTest() throws Exception {
    if (registeredAction) {
      ActionManager.getInstance().unregisterAction(getAction());
      registeredAction = false;
    }

    tearDown();
  }

  @Override
  public final String getTestDataPath() {
    return System.getProperty("user.dir");
  }
}
