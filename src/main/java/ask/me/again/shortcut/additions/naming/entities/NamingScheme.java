package ask.me.again.shortcut.additions.naming.entities;

public interface NamingScheme {

  String apply(String text);

  String getName();

  default Character findSeparator(String text) {
    if (text.contains("_")) {
      return '_';
    } else if (text.contains("-")) {
      return '-';
    } else if (text.contains(".")) {
      return '.';
    } else {
      return 'n';
    }
  }
}
