var output = ComplexOutputs.builder()
    .orig1(input.getNumber1())
    .orig2(input.getNumber1())
    .mul(Multiplication.builder()
      .nested1(input.getAnotherZero().getAnotherOne().getAnotherOneOne())
      .nested2(input.getNumber1())
      .build())
    .build();