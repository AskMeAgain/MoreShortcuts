package ask.me.again.shortcut.additions.mapstructbuilder;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static ask.me.again.shortcut.additions.mapstructbuilder.LombokToMapStructTemplate.MAPPING_TEMPLATE;
import static ask.me.again.shortcut.additions.mapstructbuilder.LombokToMapStructTemplate.TEMPLATE;

public class LombokToMapStructUtils {

  public static String buildMapper(String[] split, String packageName) {
    var mappings = getMappings(split);

    var outputType = LombokToMapStructUtils.findOutputType(split[0]);

    return TEMPLATE
        .replace("$PACKAGE", packageName)
        .replace("$OUTPUT_TYPE", outputType)
        .replace("$MAPPINGS", String.join("\n", mappings));
  }

  public static String findOutputType(String line) {
    var matcher = Pattern.compile(".* (.*).builder\\(\\)$").matcher(line);

    if (!matcher.find()) {
      return line;
    }

    return matcher.group(1);
  }

  public static String findTarget(String line) {
    var matcher = Pattern.compile("\\.(.*?)\\(").matcher(line);

    if (!matcher.find()) {
      return line;
    }

    return matcher.group(1);
  }

  public static String findSource(String line) {
    var matcher = Pattern.compile("\\.get(.*?)\\(\\)").matcher(line);
    var result = new ArrayList<String>();

    while (matcher.find()) {
      result.add(makeLowerCase(matcher.group(1)));
    }
    return String.join(".", result);
  }

  private static String makeLowerCase(String input) {
    return String.valueOf(input.charAt(0)).toLowerCase() + input.substring(1);
  }

  private static ArrayList<String> getMappings(String[] split) {
    var mappings = new ArrayList<String>();
    var stack = new ArrayList<String>();

    for (int i = 1; i < split.length - 1; i++) {
      var mappingLine = split[i];

      var target = LombokToMapStructUtils.findTarget(mappingLine);
      var source = LombokToMapStructUtils.findSource(mappingLine);

      if (mappingLine.contains(".builder()") || mappingLine.contains(".toBuilder()")) {
        stack.add(target);
      } else if (mappingLine.contains(".build())")) {
        stack.remove(stack.size() - 1);
      } else {
        var tempList = new ArrayList<>(stack);
        tempList.add(target);

        var template = MAPPING_TEMPLATE
            .replace("$OUTPUT_NAME", String.join(".", tempList))
            .replace("$INPUT_NAME", source);
        mappings.add(template);
      }
    }

    return mappings;
  }
}
