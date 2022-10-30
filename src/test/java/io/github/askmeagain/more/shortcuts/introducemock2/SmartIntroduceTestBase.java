package io.github.askmeagain.more.shortcuts.introducemock2;

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class SmartIntroduceTestBase extends LightJavaCodeInsightFixtureTestCase {

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
}
