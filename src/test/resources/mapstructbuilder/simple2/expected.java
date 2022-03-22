package test.package.abc;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ComplexOutputsMapper {
  @Mapping(target = "number1mul2", source = "number1")
  @Mapping(target = "number1mul4", source = "number1")
  ComplexOutputs map(Object input, Object input2);

}