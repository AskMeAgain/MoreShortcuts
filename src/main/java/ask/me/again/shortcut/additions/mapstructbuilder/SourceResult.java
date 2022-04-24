package ask.me.again.shortcut.additions.mapstructbuilder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SourceResult {

  boolean isSource;
  boolean isConstant;


  String line;

}
