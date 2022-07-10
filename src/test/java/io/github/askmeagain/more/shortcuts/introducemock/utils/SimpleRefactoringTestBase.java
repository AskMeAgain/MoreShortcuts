package io.github.askmeagain.more.shortcuts.introducemock.utils;

import com.intellij.ide.CopyPasteManagerEx;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.intellij.util.ui.TextTransferable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestInfo;

import java.util.ArrayList;

public abstract class SimpleRefactoringTestBase extends LightJavaCodeInsightFixtureTestCase {

  protected final static String MODULE = "/io/github/askmeagain/more/shortcuts/introducemock";

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
      myFixture.checkResultByFile(String.format("/src/test/resources/%s/%s/expected.java", testClassName, caseName));
    });
  }

  public abstract String getAction();

  @BeforeEach
  void setupTest() throws Exception {
    setUp();
  }

  @AfterEach
  void teardownTest() throws Exception {
    tearDown();
  }

  @Override
  public final String getTestDataPath() {
    return System.getProperty("user.dir");
  }
}
