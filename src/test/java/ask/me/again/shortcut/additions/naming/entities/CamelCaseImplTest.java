package ask.me.again.shortcut.additions.naming.entities;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.Assert.assertEquals;

class CamelCaseImplTest {

  @ParameterizedTest
  @CsvSource({
      "camel_case_test",
      "Camel_case_test",
      "camel_Case_Test",
      "camel.case.test",
      "camel.caSe.test",
      "Camel.caSe.test",
      "camel-case-test",
      "camel-caSe-test",
      "Camel-caSe-test",
      "camelCaseTest",
      "CamelCaseTest"
  })
  void apply(String input) {
    //Arrange --------------------------------------------------------------------------------
    var camelCase = new CamelCaseImpl();

    //Act ------------------------------------------------------------------------------------
    var snakeCaseResult = camelCase.apply(input);

    //Assert ---------------------------------------------------------------------------------
    assertEquals("camelCaseTest", snakeCaseResult);
  }
}