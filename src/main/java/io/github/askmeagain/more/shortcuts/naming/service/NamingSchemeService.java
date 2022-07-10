package io.github.askmeagain.more.shortcuts.naming.service;

import io.github.askmeagain.more.shortcuts.settings.PersistenceManagementService;
import io.github.askmeagain.more.shortcuts.naming.entities.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class NamingSchemeService {

  private final List<NamingScheme> schemes = new ArrayList<>();

  public NamingSchemeService() {

    var state = PersistenceManagementService.getInstance().getState();

    if (state.getCamelCaseImpl()) {
      schemes.add(new CamelCaseImpl());
    }
    if (state.getPascalCaseImpl()) {
      schemes.add(new PascalCaseImpl());
    }
    if (state.getDoenerCaseImpl()) {
      schemes.add(new DoenerCaseImpl());
    }
    if (state.getSnakeCaseImpl()) {
      schemes.add(new SnakeCaseImpl());
    }
    if (state.getDotCaseImpl()) {
      schemes.add(new DotCaseImpl());
    }
  }

  public String applyNext(String text, int index) {
    if (schemes.isEmpty()) {
      return text;
    }

    return Stream.of(schemes, schemes)
        .flatMap(Collection::stream)
        .skip(index % schemes.size())
        .map(x -> x.apply(text))
        .findFirst()
        .orElse(text);
  }

  public static String makeUpperCaseNextToSeparator(String text, Character separator) {
    var result = new StringBuilder();
    char[] charArray = text.toCharArray();

    result.append(charArray[0]);
    for (int i = 1; i < charArray.length; i++) {
      if (charArray[i - 1] == separator) {
        result.append(Character.toUpperCase(charArray[i]));
      } else {
        result.append(charArray[i]);
      }
    }
    return result.toString();
  }

  public static String makeFirstLetterUppercase(String text) {
    var firstChar = String.valueOf(text.charAt(0)).toUpperCase();
    return firstChar + text.substring(1);
  }

  public static String makeFirstLetterLowercase(String text) {
    var firstChar = String.valueOf(text.charAt(0)).toLowerCase();
    return firstChar + text.substring(1);
  }
}
