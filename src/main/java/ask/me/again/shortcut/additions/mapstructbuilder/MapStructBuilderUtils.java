package ask.me.again.shortcut.additions.mapstructbuilder;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class MapStructBuilderUtils {

  public static String findOutputType(String line) {
    var matcher = Pattern.compile(".* (.*).builder\\(\\)$").matcher(line);

    if (!matcher.find()) {
      return line;
    }

    return matcher.group(1);
  }

  public static String findTarget(String line) {
    var inputPattern = Pattern.compile("\\.(.*?)\\(");
    var matcher1 = inputPattern.matcher(line);

    if(!matcher1.find()){
      return line;
    }

    return matcher1.group(1);
  }

  public static String findSource(String line) {
    var getPattern = Pattern.compile("\\.get(.*?)\\(\\)");
    var matcher = getPattern.matcher(line);

    var result = new ArrayList<String>();
    while (matcher.find()) {
      result.add(makeLowerCase(matcher.group(1)));
    }
    return String.join(".", result);
  }

  private static String makeLowerCase(String input) {
    return String.valueOf(input.charAt(0)).toLowerCase() + input.substring(1);
  }

}
