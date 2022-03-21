package ask.me.again.shortcut.additions.mapstructbuilder;

public class LombokToMapStructTemplate {

  public static String TEMPLATE = "" +
      "package $PACKAGE;\n" +
      "\n" +
      "import org.mapstruct.Mapper;\n" +
      "import org.mapstruct.Mapping;\n" +
      "\n" +
      "@Mapper\n" +
      "public interface $OUTPUT_TYPEMapper {\n" +
      "$MAPPINGS\n" +
      "  $OUTPUT_TYPE map(Object input);\n" +
      "\n" +
      "}";

  public static String MAPPING_TEMPLATE =
      "  @Mapping(target = \"$OUTPUT_NAME\", source = \"$INPUT_NAME\")";
}
