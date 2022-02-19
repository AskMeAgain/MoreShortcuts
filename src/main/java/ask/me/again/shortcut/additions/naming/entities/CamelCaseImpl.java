package ask.me.again.shortcut.additions.naming.entities;

import ask.me.again.shortcut.additions.naming.service.NamingSchemeService;

public class CamelCaseImpl implements NamingScheme {

  @Override
  public String apply(String text) {
    var separator = findSeparator(text);

    switch (separator) {
      case '.':
        var result = NamingSchemeService.makeUpperCaseNextToSeparator(text.toLowerCase(), separator)
            .replaceAll("\\.", "");
        return NamingSchemeService.makeFirstLetterLowercase(result);
      case '_':
      case '-':
        var result2 = NamingSchemeService.makeUpperCaseNextToSeparator(text.toLowerCase(), separator)
            .replaceAll(String.valueOf(separator), "");
        return NamingSchemeService.makeFirstLetterLowercase(result2);
      default:
        return NamingSchemeService.makeFirstLetterLowercase(text);
    }
  }

  @Override
  public String getName() {
    return "camelCase";
  }
}
