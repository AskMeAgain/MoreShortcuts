package ask.me.again.shortcut.additions.mapstructbuilder;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapStructBuilderUtilsTest {

  @ParameterizedTest
  @CsvSource({
      "return Abc.builder(),Abc"
  })
  void findOutputType(String line, String expected) {
    //Arrange --------------------------------------------------------------------------------
    //Act ------------------------------------------------------------------------------------
    var result = LombokToMapStructUtils.findOutputType(line);

    //Assert ---------------------------------------------------------------------------------
    assertEquals(result, expected);
  }

  @ParameterizedTest
  @CsvSource({
      ".abc2(input.getAbc2().getAnotherOne()),abc2"
  })
  void findTarget(String line, String expected) {
    //Arrange --------------------------------------------------------------------------------
    //Act ------------------------------------------------------------------------------------
    var result = LombokToMapStructUtils.findTarget(line);

    //Assert ---------------------------------------------------------------------------------
    assertEquals(result, expected);
  }

  @ParameterizedTest
  @CsvSource({
      ".abc2(input.getAbc2().getAnotherOne()),abc2.anotherOne",
      ".abc2(input.getAbc2()),abc2",
  })
  void findSource(String line, String expected) {
    //Arrange --------------------------------------------------------------------------------
    //Act ------------------------------------------------------------------------------------
    var result = LombokToMapStructUtils.findSource(line);
    //Assert ---------------------------------------------------------------------------------
    assertEquals(result, expected);

  }
}