package ask.me.again.shortcut.additions.naming.entities;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.Assert.assertEquals;

class DotCaseImplTest {

  @ParameterizedTest
  @CsvSource({
      "dot_case_test,false",
      "dot_caSe_test,false",
      "dot.case.test,true",
      "dot.caSe.test,false",
      "dot-case-test,false",
      "dot-caSe-test,false",
      "dotCaseTest,false",
      "DotCaseTest,false"
  })
  void apply(String input, boolean isOfType) {
    //Arrange --------------------------------------------------------------------------------
    var dotCase = new DotCaseImpl();

    //Act ------------------------------------------------------------------------------------
    var snakeCaseResult = dotCase.apply(input);
    var resultApplicable = dotCase.isOfType(input);

    //Assert ---------------------------------------------------------------------------------
    assertEquals("dot.case.test", snakeCaseResult);
    assertEquals(isOfType, resultApplicable);

  }
}