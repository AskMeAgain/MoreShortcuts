package ask.me.again.shortcut.additions.introducemock;

import ask.me.again.shortcut.additions.introducemock.helpers.ExecutionType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiParameter;
import org.apache.commons.lang3.tuple.Pair;

public interface IntroduceMockImplementation {

  Pair<PsiParameter[], ExecutionType> getPsiParameters(PsiExpressionList expressionList);

  PsiElement findAnchor(PsiExpressionList expressionList);

  boolean isType(PsiExpressionList expressionList);

}
