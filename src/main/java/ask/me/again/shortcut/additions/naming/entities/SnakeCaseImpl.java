package ask.me.again.shortcut.additions.naming.entities;

import ask.me.again.shortcut.additions.naming.NamingSchemeUtils;

public class SnakeCaseImpl implements NamingScheme {

  private static final String PATTERN = "^[A-Z]+(?:[_][A-Z]+)*$";

  @Override
  public boolean isOfType(String text) {
    return text.matches(PATTERN);
  }

  @Override
  public String apply(String text) {

    var separator = NamingSchemeUtils.findSeparator(text);

    switch (separator) {
      case '_':
        return text.toUpperCase();
      case '.':
        return text.replaceAll("\\.", "_").toUpperCase();
      case '-':
        return text.replaceAll("-", "_").toUpperCase();
      default:
        return (text.charAt(0) + text.substring(1).replaceAll("([A-Z])", "_$1")).toUpperCase();
    }
  }
}
