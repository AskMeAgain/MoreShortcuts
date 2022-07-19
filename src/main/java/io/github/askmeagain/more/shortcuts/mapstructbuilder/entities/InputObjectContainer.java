package io.github.askmeagain.more.shortcuts.mapstructbuilder.entities;


import com.intellij.psi.PsiType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InputObjectContainer {

  PsiType type;
  String varName;

  @Override
  public String toString() {
    return type.getPresentableText() + " " + varName;
  }

}
