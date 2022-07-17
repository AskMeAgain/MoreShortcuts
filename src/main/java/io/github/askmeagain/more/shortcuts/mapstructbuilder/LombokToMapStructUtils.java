package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LombokToMapStructUtils {

  public static String makeLowerCase(String input) {
    return String.valueOf(input.charAt(0)).toLowerCase() + input.substring(1);
  }
}
