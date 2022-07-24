package io.github.askmeagain.more.shortcuts.mapstructbuilder.entities;

public class LombokToMapStructTemplate {

  public static String TEMPLATE = "" +
      "$IN_PACKAGE\n" +
      "\n" +
      "$IMPORTS" +
      "\n" +
      "\n" +
      "@Mapper\n" +
      "public interface $OUTPUT_TYPEMapper {\n" +
      "\n" +
      "$MAPPING_METHODS" +
      "\n" +
      "$OVERRIDE_METHODS" +
      "}";

  public static String OVERRIDE_TEMPLATE = "  @Named(\"$METHOD_NAME\")\n  default $OUTPUT_TYPE $METHOD_NAME ($METHOD_INPUT_TYPES){\n    return $CODE;\n  }\n";
}
