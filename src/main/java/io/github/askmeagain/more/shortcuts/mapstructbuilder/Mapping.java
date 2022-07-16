package io.github.askmeagain.more.shortcuts.mapstructbuilder;

import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Mapping {

  PsiExpressionList source;
  @Singular
  List<String> targets;
  String constant;

}
