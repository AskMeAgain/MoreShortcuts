var output = ComplexOutputs.builder()
    .orig1(input.getNumber1())
    .orig2(input.getNumber1())
    .mul(anotherObject.getXyz().toBuilder()
      .nested1(input2.getAnotherZero().getAnotherOne().getAnotherOneOne())
      .nested2(input4.getNumber1())
      .build())
    .build();