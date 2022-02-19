package ask.me.again.shortcut.additions.naming.entities;

public class SnakeCaseImpl implements NamingScheme {

  @Override
  public String apply(String text) {
    switch (findSeparator(text)) {
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

  @Override
  public String getName() {
    return "SNAKE_CASE";
  }
}
