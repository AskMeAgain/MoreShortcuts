package ask.me.again.shortcut.additions.smartpaste.insertions;

public interface SmartInsertion {

  boolean isApplicable(String originalText, String clipboard);

  String apply(String originalText, String clipboard);

}
