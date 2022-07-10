package io.github.askmeagain.more.shortcuts.naming.entities;

public class DoenerCaseImpl implements NamingScheme {

  @Override
  public String apply(String text) {
    switch (findSeparator(text)) {
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

  @Override
  public String getName() {
    return "doener-case";
  }
}
