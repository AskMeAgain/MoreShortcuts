package ask.me.again.shortcut.additions.naming.entities;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.Assert.assertEquals;

class DoenerCaseImplTest {

  @ParameterizedTest
  @CsvSource({
      "doener_case_test,false",
      "doener_caSe_test,false",
      "doener.case.test,false",
      "doener.caSe.test,false",
      "doener-case-test,true",
      "doener-caSe-test,false",
      "doenerCaseTest,false",
      "DoenerCaseTest,false"
  })
  void apply(String input, boolean isOfType) {
    //Arrange --------------------------------------------------------------------------------
    var doenerCase = new DoenerCaseImpl();

    //Act ------------------------------------------------------------------------------------
    var snakeCaseResult = doenerCase.apply(input);
    var resultApplicable = doenerCase.isOfType(input);

    //Assert ---------------------------------------------------------------------------------
    assertEquals("doener-case-test", snakeCaseResult);
    assertEquals(isOfType, resultApplicable);

  }
}