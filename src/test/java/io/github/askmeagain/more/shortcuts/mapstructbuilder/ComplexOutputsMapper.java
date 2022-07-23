package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.MapperTest.InputContainer;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.MapperTest.ComplexOutputs;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.MapperTest.Multiplication;
import org.mapstruct.Named;

@Mapper
public interface ComplexOutputsMapper {

  @Mapping(target = "nested1", source ="input.anotherZero.anotherOne.anotherOneOne")
  @Mapping(target = "nested2", source ="input.number1")
  @Mapping(target = "nested3", source = "input", qualifiedByName="getNested3")
  @Mapping(target = "nested4", constant="what a nice constant")
  @Mapping(target = "nested5", constant="1")
  @Mapping(target = "nested6", expression="java(getNested6(input2, input))")
  Multiplication mapMultiplication(InputContainer input2, InputContainer input);

  @Mapping(target = "mul", source ="input.anotherZero.anotherOne.anotherOneOne")
  @Mapping(target = "orig1", source ="input.number1")
  @Mapping(target = "orig2", source ="input.number1")
  ComplexOutputs mapComplexOutputs(InputContainer input2, InputContainer input);

  @Named("getNested3")
  default int getNested3 (InputContainer input){
    return input.getNumber2() + input.getNumber2();
  }

  @Named("getNested6")
  default int getNested6 (InputContainer input2, InputContainer input){
    return input.getNumber2() + input2.getNumber2();
  }
}