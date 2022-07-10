package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SourceResult {

  boolean isSource;
  boolean isConstant;


  String line;

}
