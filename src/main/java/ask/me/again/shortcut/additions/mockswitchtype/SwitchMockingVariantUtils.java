package ask.me.again.shortcut.additions.mockswitchtype;

import java.util.regex.Pattern;

public class SwitchMockingVariantUtils {

  private static final Pattern case1Pattern = Pattern.compile("^.*when\\((.*)\\.(.*\\(.*\\))\\)\\.thenReturn\\((.*)\\)");
  private static final Pattern case2Pattern = Pattern.compile("^.*doReturn\\((.*)\\)\\.when\\((.*)\\)\\.(.*\\(.*\\))");

  public static String convertLine(String text, Boolean addStaticImport) {
    var matcherCase1 = case1Pattern.matcher(text);
    var matcherCase2 = case2Pattern.matcher(text);

    if (matcherCase1.find() && matcherCase1.groupCount() == 3) {
      var mockName = matcherCase1.group(1);
      var methodName = matcherCase1.group(2);
      var object = matcherCase1.group(3);
      text = String.format("Mockito.doReturn(%s).when(%s).%s;", object, mockName, methodName);
    } else if (matcherCase2.find() && matcherCase2.groupCount() == 3) {
      var object = matcherCase2.group(1);
      var mockName = matcherCase2.group(2);
      var methodName = matcherCase2.group(3);
      text = String.format("Mockito.when(%s.%s).thenReturn(%s);", mockName, methodName, object);
    }

    if (addStaticImport) {
      text = text.replaceAll("Mockito\\.", "");
    }

    return text;
  }
}
