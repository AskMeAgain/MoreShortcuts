package ask.me.again.shortcut.additions.naming.entities;

import ask.me.again.shortcut.additions.naming.NamingSchemeUtils;

public class DoenerCaseImpl implements NamingScheme {

  @Override
  public String apply(String text) {

    var separator = NamingSchemeUtils.findSeparator(text);

    switch (separator) {
      case '_':
        return text.replaceAll("_", "-").toLowerCase();
      case '.':
        return text.replaceAll("\\.", "-").toLowerCase();
      case '-':
        return text.toLowerCase();
      default:
        return (text.charAt(0) + text.substring(1).replaceAll("([A-Z])", "-$1")).toLowerCase();
    }
  }
}
