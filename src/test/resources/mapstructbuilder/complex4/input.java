var output = ComplexOutputs.builder()
    .orig1(anotherOne)
    .orig2(input.getNumber1())
    .mul(anotherObject.getXyz().toBuilder()
      .nested1(input2.getAnotherZero().getAnotherOne().getAnotherOneOne())
      .nested2("a")
      .build())
    .build();