package test.package.abc;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ComplexOutputsMapper {
  @Mapping(target = "number1mul2", source = "input.number1")
  @Mapping(target = "number1mul4", source = "input.number1")
  ComplexOutputs map(Object input);

}