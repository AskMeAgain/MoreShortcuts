package ask.me.again.shortcut.additions.introducemock.impl.targets;

import ask.me.again.shortcut.additions.introducemock.impl.IntroduceTarget;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiParameter;

import java.util.ArrayList;
import java.util.List;

public abstract class TargetBase implements IntroduceTarget {

  protected final PsiElementFactory factory;

  public TargetBase(Project project){
    this.factory = JavaPsiFacade.getElementFactory(project);;
  }

  public List<PsiElement> createMockExpressions(PsiParameter[] parameterList, List<Boolean> changeMap){
    var resultList = new ArrayList<PsiElement>();

    for (int i = 0; i < parameterList.length; i++) {
      if (changeMap.get(i)) {
        resultList.add(createExpression(parameterList[i]));
      } else {
        resultList.add(null);
      }
    }

    return resultList;
  }

}
