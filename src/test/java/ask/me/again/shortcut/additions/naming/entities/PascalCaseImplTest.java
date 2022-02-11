package ask.me.again.shortcut.additions.naming.entities;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.Assert.assertEquals;

class PascalCaseImplTest {

  @ParameterizedTest
  @CsvSource({
      "pascal_case_test,false",
      "paScal_Case_Test,false",
      "pascal.case.test,false",
      "pascal.caSe.test,false",
      "pascal-case-test,false",
      "pascal-caSe-test,false",
      "pascalCaseTest,false",
      "PascalCaseTest,true"
  })
  void apply(String input, boolean isOfType) {
    //Arrange --------------------------------------------------------------------------------
    var pascalCase = new PascalCaseImpl();

    //Act ------------------------------------------------------------------------------------
    var snakeCaseResult = pascalCase.apply(input);
    var resultApplicable = pascalCase.isOfType(input);

    //Assert ---------------------------------------------------------------------------------
    assertEquals("PascalCaseTest", snakeCaseResult);
    assertEquals(isOfType, resultApplicable);

  }
}