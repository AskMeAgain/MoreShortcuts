package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.MapperTest.ComplexOutputs;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.MapperTest.InputContainer;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.MapperTest.Multiplication;
import io.github.askmeagain.more.shortcuts.mapstructbuilder.MapperTest.NestedSubtractions;
import org.mapstruct.Named;

@Mapper
public interface ComplexOutputsMapper {

  ComplexOutputsMapper INSTANCE = Mappers.getMapper(ComplexOutputsMapper.class);

  @Mapping(target = "nested5", constant="1")
  @Mapping(target = "nested4", constant="what a nice constant")
  @Mapping(target = "nested3", source = "input", qualifiedByName="getNested3")
  @Mapping(target = "nested2", source ="input.number1")
  @Mapping(target = "nested1", source ="input.anotherZero.anotherOne.anotherOneOne")
  @Mapping(target = "nestedSubtractions", source = "input")
  @Mapping(target = "nested6", expression="java(getNested6(input2, input))")
  Multiplication mapMultiplication(InputContainer input2, InputContainer input);

  @Mapping(target = "nested2", source ="input.anotherOneOne")
  @Mapping(target = "nested1", constant="abc")
  NestedSubtractions mapNestedSubtractions(InputContainer input);

  @Mapping(target = "orig2", source ="input.number1")
  @Mapping(target = "orig1", source ="input.number1")
  @Mapping(target = "mul", expression="java(mapMultiplication(input, input2))")
  ComplexOutputs mapComplexOutputs(InputContainer input, InputContainer input2);

  @Named("getNested3")
  default int getNested3 (InputContainer input){
    return input.getNumber2() + input.getNumber2();
  }

  @Named("getNested6")
  default int getNested6 (InputContainer input2, InputContainer input){
    return input.getNumber2() + input2.getNumber2();
  }
}