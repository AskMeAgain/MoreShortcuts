package ask.me.again.shortcut.additions.naming.entities;

public interface NamingScheme {

  boolean isOfType(String text);

  String apply(String text);
}
