package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LombokToMapStructUtils {

  public static String buildMapper(String[] split, String packageName) {
    var mappings = getMappings(split);

    var outputType = LombokToMapStructUtils.findOutputType(split[0]);

    return LombokToMapStructTemplate.TEMPLATE
        .replace("$IN_PACKAGE", !Strings.isNullOrEmpty(packageName) ? "package $PACKAGE;" : "")
        .replace("$PACKAGE", packageName)
        .replace("$INPUTS", mappings.getInputObjects().stream()
            .filter(Objects::nonNull)
            .map(obj -> "Object " + obj)
            .collect(Collectors.joining(", ")))
        .replace("$OUTPUT_TYPE", outputType)
        .replace("$MAPPINGS", String.join("\n", mappings.getMappings()));
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

  public static SourceResult findSource(String line) {
    var matcher = Pattern.compile("\\.get(.*?)\\(\\)").matcher(line);
    var result = new ArrayList<String>();

    while (matcher.find()) {
      result.add(makeLowerCase(matcher.group(1)));
    }

    if (result.isEmpty()) {
      var all = Pattern.compile("\\((.*?)\\)").matcher(line);
      if (all.find()) {
        var group = all.group(1);
        result.add(group);

        if (group.startsWith("\"") && group.endsWith("\"")) {
          var joinedConstant = String.join(".", result);
          return SourceResult.builder()
              .line(joinedConstant.substring(1, joinedConstant.length() - 1))
              .isSource(false)
              .isConstant(true)
              .build();
        }
      }
    }

    return SourceResult.builder()
        .line(String.join(".", result))
        .isSource(true)
        .isConstant(false)
        .build();
  }

  public static String findSourceOrigin(String line) {
    var matcher = Pattern.compile("\\((.*?)\\.get").matcher(line);

    if (matcher.find()) {
      return matcher.group(1);
    }

    return null;
  }

  private static String makeLowerCase(String input) {
    return String.valueOf(input.charAt(0)).toLowerCase() + input.substring(1);
  }

  private static MappingContainer getMappings(String[] split) {
    var stack = new ArrayList<String>();

    var container = MappingContainer.builder();

    for (int i = 1; i < split.length - 1; i++) {
      var mappingLine = split[i];

      var target = LombokToMapStructUtils.findTarget(mappingLine);
      var source = LombokToMapStructUtils.findSource(mappingLine);
      var sourceOrigin = LombokToMapStructUtils.findSourceOrigin(mappingLine);

      if (mappingLine.contains(".builder()") || mappingLine.contains(".toBuilder()")) {
        stack.add(target);
      } else if (mappingLine.contains(".build())")) {
        stack.remove(stack.size() - 1);
      } else {
        var tempList = new ArrayList<>(stack);
        tempList.add(target);

        SourceResult inputName;
        if (sourceOrigin == null) {
          inputName = source;
          if (source.isSource() && !source.getLine().equals("")) {
            container.inputObject(source.getLine());
          }
        } else {
          inputName = SourceResult.builder()
              .line(sourceOrigin + "." + source.getLine())
              .isConstant(false)
              .isSource(true)
              .build();
        }

        var template = LombokToMapStructTemplate.MAPPING_TEMPLATE
            .replace("$SOURCE", inputName.isSource() ? ", source = \"$INPUT_NAME\"" : "")
            .replace("$CONSTANT", inputName.isConstant() ? ", constant = \"$CONSTANT_NAME\"" : "")
            .replace("$OUTPUT_NAME", String.join(".", tempList))
            .replace("$INPUT_NAME", inputName.getLine())
            .replace("$CONSTANT_NAME", inputName.getLine());
        container.inputObject(sourceOrigin);
        container.mapping(template);
      }
    }

    return container.build();
  }
}
