package ask.me.again.shortcut.additions.introducemock.utils;

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestInfo;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class SimpleRefactoringTestBase extends LightJavaCodeInsightFixtureTestCase {

  protected final static String MODULE = "/ask/me/again/shortcut/additions/introducemock";

  public Iterable<DynamicTest> dynamicTestsWithIterable(TestInfo testInfo, String... testcases) throws Exception {
    setUp();

    var testName = testInfo.getTestMethod().get().getName();

    return Arrays.stream(testcases)
        .map(x -> createDynamicTest(testName, x))
        .collect(Collectors.toList());
  }

  private DynamicTest createDynamicTest(String testClassName, String caseName) {
    return DynamicTest.dynamicTest(caseName, () ->
    {
      myFixture.configureByFiles(
          String.format("/src/test/resources/%s/%s/input.java", testClassName, caseName),
          "/src/test/java" + MODULE + "/entities/TestClass.java",
          "/src/test/resources/mockito.java",
          "/src/test/resources/mock.java"
      );

      myFixture.performEditorAction(getAction());
      myFixture.checkResultByFile(String.format("/src/test/resources/%s/%s/expected.java", testClassName, caseName));
    });
  }

  public abstract String getAction();

  @Override
  public String getTestDataPath() {
    return System.getProperty("user.dir");
  }
}
