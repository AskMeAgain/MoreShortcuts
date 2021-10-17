package ask.me.again.shortcut.additions.introducemock.utils;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestInfo;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class SimpleRefactoringTestBase extends LightJavaCodeInsightFixtureTestCase {

  private final static String MODULE = "/ask/me/again/shortcut/additions/introducemock";

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

      myFixture.testAction(getAction());
      checkResult(testClassName, caseName);
    });
  }

  public abstract AnAction getAction();

  private void checkResult(String testClassName, String caseName) {
    myFixture.checkResultByFile(String.format("/src/test/resources/%s/%s/expected.java", testClassName, caseName));
  }

  @Override
  public String getTestDataPath() {
    return System.getProperty("user.dir");
  }
}
