package ask.me.again.shortcut.additions.introducemock;

import ask.me.again.shortcut.additions.introducemock.helpers.ExecutionType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiParameter;
import org.apache.commons.lang3.tuple.Pair;

public interface IntroduceMockImplementation {

  PsiParameter[] getPsiParameters(PsiExpressionList expressionList) throws MultipleResultException;

  PsiElement findAnchor(PsiExpressionList expressionList);

  boolean isType(PsiExpressionList expressionList);

}
