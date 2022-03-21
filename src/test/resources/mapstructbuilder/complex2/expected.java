package test.package.abc;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ComplexOutputsMapper {
  @Mapping(target = "orig1", source = "number1")
  @Mapping(target = "orig2", source = "number1")
  @Mapping(target = "mul.nested1", source = "anotherZero.anotherOne.anotherOneOne")
  @Mapping(target = "mul.nested2", source = "number1")
  ComplexOutputs map(Object input);

}