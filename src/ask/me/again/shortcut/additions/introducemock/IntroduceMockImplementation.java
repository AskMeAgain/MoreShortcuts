package ask.me.again.shortcut.additions.introducemock;

import ask.me.again.shortcut.additions.introducemock.exceptions.ClassFromTypeNotFoundException;
import ask.me.again.shortcut.additions.introducemock.exceptions.MultipleResultException;
import ask.me.again.shortcut.additions.introducemock.exceptions.PsiTypeNotFoundException;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiParameter;

public interface IntroduceMockImplementation {

  PsiParameter[] getPsiParameters(PsiExpressionList expressionList) throws MultipleResultException, PsiTypeNotFoundException, ClassFromTypeNotFoundException;

  PsiElement findAnchor(PsiExpressionList expressionList);

  boolean isType(PsiExpressionList expressionList);

}
