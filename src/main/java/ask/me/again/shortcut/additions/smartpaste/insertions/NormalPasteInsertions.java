package ask.me.again.shortcut.additions.smartpaste.insertions;

public class NormalPasteInsertions implements SmartInsertion {
  @Override
  public boolean isApplicable(String originalText, String text) {
    return true;
  }

  @Override
  public String apply(String originalText, String clipboard) {
    return clipboard;
  }
}
