package ask.me.again.shortcut.additions.naming.entities;

import ask.me.again.shortcut.additions.naming.NamingSchemeUtils;

public class PascalCaseImpl implements NamingScheme {

  private static final String PATTERN = "([A-Z][a-z0-9]+)((\\d)|([A-Z0-9][a-z0-9]+))*([A-Z])?";

  @Override
  public boolean isOfType(String text) {
    return text.matches(PATTERN);
  }

  @Override
  public String apply(String text) {

    var separator = NamingSchemeUtils.findSeparator(text);

    switch (separator) {
      case '.':
        var result = NamingSchemeUtils.makeUpperCaseNextToSeparator(text.toLowerCase(), separator)
            .replaceAll("\\.", "");
        return NamingSchemeUtils.makeFirstLetterUppercase(result);
      case '_':
      case '-':
        var result2 = NamingSchemeUtils.makeUpperCaseNextToSeparator(text.toLowerCase(), separator)
            .replaceAll(String.valueOf(separator), "");
        return NamingSchemeUtils.makeFirstLetterUppercase(result2);
      default:
        return NamingSchemeUtils.makeFirstLetterUppercase(text);
    }
  }
}
