package ask.me.again.shortcut.additions.mapstructbuilder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;

public class MapStructTests {

  private final ClassLoader classLoader = MapStructTests.class.getClassLoader();

  @ParameterizedTest
  @CsvSource({
      "simple1", "simple2", "complex1", "complex2", "complex3"
  })
  void test(String testCase) throws IOException {
    //Arrange --------------------------------------------------------------------------------
    var input = classLoader.getResourceAsStream("mapstructbuilder/" + testCase + "/input.java").readAllBytes();
    var expected = classLoader.getResourceAsStream("mapstructbuilder/" + testCase + "/expected.java").readAllBytes();

    //Act ------------------------------------------------------------------------------------
    var result = LombokToMapStructUtils.buildMapper(new String(input).split("\n"), "test.package.abc");

    //Assert ---------------------------------------------------------------------------------
    Assertions.assertEquals(new String(expected), result);
  }
}
