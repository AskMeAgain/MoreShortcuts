package io.github.askmeagain.more.shortcuts.mapstructbuilder;

public class LombokToMapStructTemplate {

  public static String TEMPLATE = "" +
      "$IN_PACKAGE\n" +
      "\n" +
      "$IMPORTS" +
      "\n" +
      "@Mapper\n" +
      "public interface $OUTPUT_TYPEMapper {\n" +
      "$MAPPINGS\n" +
      "  $OUTPUT_TYPE map($INPUTS);\n" +
      "\n" +
      "$OVERRIDE_METHODS" +
      "}";

  public static String MAPPING_TEMPLATE = "  @Mapping(target = \"$TARGET\"$SOURCE$CONSTANT)";

  public static String OVERRIDE_TEMPLATE = "  @Named(\"$METHOD_NAME\")\n  default $OUTPUT_TYPE $METHOD_NAME ($METHOD_INPUT_TYPES){\n    return $CODE;\n  }\n";
}
