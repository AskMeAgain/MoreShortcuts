package ask.me.again.shortcut.additions.naming.entities;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.Assert.assertEquals;

class SnakeCaseImplTest {

  @ParameterizedTest
  @CsvSource({
      "SNAKE_CASE_TEST,true",
      "snake_case_test,false",
      "snake_caSe_test,false",
      "snake.case.test,false",
      "snake.caSe.test,false",
      "snake-case-test,false",
      "snake-caSe-test,false",
      "snakeCaseTest,false",
      "SnakeCaseTest,false"
  })
  void apply(String input, boolean isOfType) {
    //Arrange --------------------------------------------------------------------------------
    var snakeCase = new SnakeCaseImpl();

    //Act ------------------------------------------------------------------------------------
    var snakeCaseResult = snakeCase.apply(input);
    var resultApplicable = snakeCase.isOfType(input);

    //Assert ---------------------------------------------------------------------------------
    assertEquals("SNAKE_CASE_TEST", snakeCaseResult);
    assertEquals(isOfType, resultApplicable);

  }
}