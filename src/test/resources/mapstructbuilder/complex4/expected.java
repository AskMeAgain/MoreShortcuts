package test.package.abc;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ComplexOutputsMapper {
  @Mapping(target = "orig1", source = "anotherOne")
  @Mapping(target = "orig2", source = "input.number1")
  @Mapping(target = "mul.nested1", source = "input2.anotherZero.anotherOne.anotherOneOne")
  @Mapping(target = "mul.nested2", constant = "a")
  ComplexOutputs map(Object anotherOne, Object input, Object input2);

}