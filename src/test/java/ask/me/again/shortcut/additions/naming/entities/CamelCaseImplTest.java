package ask.me.again.shortcut.additions.naming.entities;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.Assert.assertEquals;

class CamelCaseImplTest {

  @ParameterizedTest
  @CsvSource({
      "camel_case_test,false",
      "Camel_case_test,false",
      "camel_Case_Test,false",
      "camel.case.test,false",
      "camel.caSe.test,false",
      "Camel.caSe.test,false",
      "camel-case-test,false",
      "camel-caSe-test,false",
      "Camel-caSe-test,false",
      "camelCaseTest,true",
      "CamelCaseTest,false"
  })
  void apply(String input, boolean isOfType) {
    //Arrange --------------------------------------------------------------------------------
    var camelCase = new CamelCaseImpl();

    //Act ------------------------------------------------------------------------------------
    var snakeCaseResult = camelCase.apply(input);
    var resultApplicable = camelCase.isOfType(input);

    //Assert ---------------------------------------------------------------------------------
    assertEquals("camelCaseTest", snakeCaseResult);
    assertEquals(isOfType, resultApplicable);

  }
}