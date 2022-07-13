package io.github.askmeagain.more.shortcuts.mapstructbuilder;

public class LombokToMapStructTemplate {

  public static String TEMPLATE = "" +
      "$IN_PACKAGE\n" +
      "\n" +
      "import org.mapstruct.Mapper;\n" +
      "import org.mapstruct.Mapping;\n" +
      "\n" +
      "@Mapper\n" +
      "public interface $OUTPUT_TYPEMapper {\n" +
      "$MAPPINGS\n" +
      "  $OUTPUT_TYPE map($INPUTS);\n" +
      "\n" +
      "}";

  public static String MAPPING_TEMPLATE = "  @Mapping(target = \"$OUTPUT_NAME\"$SOURCE$CONSTANT)";
}
