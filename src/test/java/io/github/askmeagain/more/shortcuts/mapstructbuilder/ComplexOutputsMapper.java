package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import io.github.askmeagain.more.shortcuts.mapstructbuilder.MapperTest.ComplexOutputs;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.MapperTest.InputContainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface ComplexOutputsMapper {
  @Mapping(target = "orig1", source = "input.number1")
  @Mapping(target = "orig2", source = "input.number1")
  @Mapping(target = "mul.nested1", source = "input.anotherZero.anotherOne.anotherOneOne")
  @Mapping(target = "mul.nested2", source = "input.number1")
  @Mapping(target = "mul.nested3", source = "input", qualifiedByName = "getNested3")
  @Mapping(target = "mul.nested4", constant = "what a nice constant")
  @Mapping(target = "mul.nested5", constant = "1")
  @Mapping(target = "mul.nested6", expression = "java(getNested6(input2, input))")
  ComplexOutputs map(InputContainer input, InputContainer input2);

  @Named("getNested3")
  default int getNested3(InputContainer input) {
    return input.getNumber2() + input.getNumber2();
  }

  @Named("getNested6")
  default int getNested6(InputContainer input2, InputContainer input) {
    return input.getNumber2() + input2.getNumber2();
  }
}