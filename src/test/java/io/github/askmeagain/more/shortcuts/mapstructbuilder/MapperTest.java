package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import lombok.*;
import lombok.experimental.SuperBuilder;

public class MapperTest {

  public Object test() {
    var input = InputContainer.builder()
        .number1("abc")
        .build();

    var input2 = InputContainer.builder()
        .number1("abc")
        .build();

    ComplexOutputs def = ComplexOutputs.builder()
        .orig1(input.getNumber1())
        .orig2(input.getNumber1())
        .mul(Multiplication.builder()
            .nested1(input.getAnotherZero().getAnotherOne().getAnotherOneOne())
            .nested2(input.getNumber1())
            .nested3(input.getNumber2() + input.getNumber2())
            .nested4("what a nice constant")
            .nested5(1)
            .nested6(input.getNumber2() + input2.getNumber2())
            .nestedSubtractions(NestedSubtractions.builder()
                .nested1("abc")
                .nested2(input.getAnotherOneOne())
                .build())
            .build())
        .build();

    return def;
  }

  @SuperBuilder
  public static class Base {
    String superbuilderBase;
  }

  @Data
  @SuperBuilder
  public static class ComplexOutputs extends Base {

    String orig1;
    String orig2;

    @Getter
    @Builder.Default
    Multiplication mul = Multiplication.builder().build();

  }

  @Data
  @Builder
  public static class InputContainer {

    String number1;
    Integer number2;
    InputContainer anotherZero;
    InputContainer anotherOne;
    String anotherOneOne;

  }

  @Data
  @Builder
  public static class Multiplication {

    String nested1;
    String nested2;
    Integer nested3;
    String nested4;
    Integer nested5;
    Integer nested6;
    NestedSubtractions nestedSubtractions;

  }

  @Data
  @Builder
  public static class NestedSubtractions {

    String nested1;
    String nested2;

  }
}
