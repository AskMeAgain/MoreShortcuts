package ask.me.again.shortcut.additions.naming.entities;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.Assert.assertEquals;

class DotCaseImplTest {

  @ParameterizedTest
  @CsvSource({
      "dot_case_test",
      "dot_caSe_test",
      "dot.case.test",
      "dot.caSe.test",
      "dot-case-test",
      "dot-caSe-test",
      "dotCaseTest",
      "DotCaseTest"
  })
  void apply(String input, boolean isOfType) {
    //Arrange --------------------------------------------------------------------------------
    var dotCase = new DotCaseImpl();

    //Act ------------------------------------------------------------------------------------
    var snakeCaseResult = dotCase.apply(input);

    //Assert ---------------------------------------------------------------------------------
    assertEquals("dot.case.test", snakeCaseResult);
  }
}