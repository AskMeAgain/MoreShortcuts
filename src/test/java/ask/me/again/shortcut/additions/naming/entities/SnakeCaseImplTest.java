package ask.me.again.shortcut.additions.naming.entities;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.Assert.assertEquals;

class SnakeCaseImplTest {

  @ParameterizedTest
  @CsvSource({
      "SNAKE_CASE_TEST",
      "snake_case_test",
      "snake_caSe_test",
      "snake.case.test",
      "snake.caSe.test",
      "snake-case-test",
      "snake-caSe-test",
      "snakeCaseTest",
      "SnakeCaseTest"
  })
  void apply(String input) {
    //Arrange --------------------------------------------------------------------------------
    var snakeCase = new SnakeCaseImpl();

    //Act ------------------------------------------------------------------------------------
    var snakeCaseResult = snakeCase.apply(input);

    //Assert ---------------------------------------------------------------------------------
    assertEquals("SNAKE_CASE_TEST", snakeCaseResult);
  }
}