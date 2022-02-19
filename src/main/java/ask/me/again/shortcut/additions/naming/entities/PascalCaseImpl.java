package ask.me.again.shortcut.additions.naming.entities;

import ask.me.again.shortcut.additions.naming.service.NamingSchemeService;

public class PascalCaseImpl implements NamingScheme {

  @Override
  public String apply(String text) {

    var separator = findSeparator(text);

    switch (separator) {
      case '.':
        var result = NamingSchemeService.makeUpperCaseNextToSeparator(text.toLowerCase(), separator)
            .replaceAll("\\.", "");
        return NamingSchemeService.makeFirstLetterUppercase(result);
      case '_':
      case '-':
        var result2 = NamingSchemeService.makeUpperCaseNextToSeparator(text.toLowerCase(), separator)
            .replaceAll(String.valueOf(separator), "");
        return NamingSchemeService.makeFirstLetterUppercase(result2);
      default:
        return NamingSchemeService.makeFirstLetterUppercase(text);
    }
  }

  @Override
  public String getName() {
    return "PascalCase";
  }
}
