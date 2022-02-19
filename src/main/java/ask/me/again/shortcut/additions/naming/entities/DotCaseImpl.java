package ask.me.again.shortcut.additions.naming.entities;

public class DotCaseImpl implements NamingScheme {

  @Override
  public String apply(String text) {
    switch (findSeparator(text)) {
      case '_':
        return text.replaceAll("_", "\\.").toLowerCase();
      case '.':
        return text.toLowerCase();
      case '-':
        return text.replaceAll("-", "\\.").toLowerCase();
      default:
        return (text.charAt(0) + text.substring(1).replaceAll("([A-Z])", ".$1")).toLowerCase();
    }
  }

  @Override
  public String getName() {
    return "dot.case";
  }
}
