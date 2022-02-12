package ask.me.again.shortcut.additions.naming.entities;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.Assert.assertEquals;

class PascalCaseImplTest {

  @ParameterizedTest
  @CsvSource({
      "pascal_case_test",
      "paScal_Case_Test",
      "pascal.case.test",
      "pascal.caSe.test",
      "pascal-case-test",
      "pascal-caSe-test",
      "pascalCaseTest",
      "PascalCaseTest",
      "pascal.case.test"
  })
  void apply(String input) {
    //Arrange --------------------------------------------------------------------------------
    var pascalCase = new PascalCaseImpl();

    //Act ------------------------------------------------------------------------------------
    var snakeCaseResult = pascalCase.apply(input);

    //Assert ---------------------------------------------------------------------------------
    assertEquals("PascalCaseTest", snakeCaseResult);
  }
}