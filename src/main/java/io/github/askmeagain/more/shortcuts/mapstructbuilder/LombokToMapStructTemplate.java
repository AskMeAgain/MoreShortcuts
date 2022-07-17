package io.github.askmeagain.more.shortcuts.mapstructbuilder;

public class LombokToMapStructTemplate {

  public static String TEMPLATE = "" +
      "$IN_PACKAGE\n" +
      "\n" +
      "import org.mapstruct.Mapper;\n" +
      "import org.mapstruct.Mapping;\n" +
      "import org.mapstruct.Named;\n" +
      "\n" +
      "@Mapper\n" +
      "public interface $OUTPUT_TYPEMapper {\n" +
      "$MAPPINGS\n" +
      "  $OUTPUT_TYPE map($INPUTS);\n" +
      "\n" +
      "$OVERRIDE_METHODS" +
      "\n" +
      "}";

  public static String MAPPING_TEMPLATE = "  @Mapping(target = \"$TARGET\"$SOURCE$CONSTANT)";

  public static String OVERRIDE_TEMPLATE = "  @Named(\"$METHOD_NAME\")\n  default $OUTPUT_TYPE $METHOD_NAME (??????????){\n    return $CODE;\n  }";
}
