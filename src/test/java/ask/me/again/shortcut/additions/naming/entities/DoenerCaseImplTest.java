package ask.me.again.shortcut.additions.naming.entities;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.Assert.assertEquals;

class DoenerCaseImplTest {

  @ParameterizedTest
  @CsvSource({
      "doener_case_test",
      "doener_caSe_test",
      "doener.case.test",
      "doener.caSe.test",
      "doener-case-test",
      "doener-caSe-test",
      "doenerCaseTest",
      "DoenerCaseTest"
  })
  void apply(String input) {
    //Arrange --------------------------------------------------------------------------------
    var doenerCase = new DoenerCaseImpl();

    //Act ------------------------------------------------------------------------------------
    var snakeCaseResult = doenerCase.apply(input);

    //Assert ---------------------------------------------------------------------------------
    assertEquals("doener-case-test", snakeCaseResult);
  }
}