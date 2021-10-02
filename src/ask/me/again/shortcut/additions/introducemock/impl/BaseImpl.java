package ask.me.again.shortcut.additions.introducemock.impl;

import ask.me.again.shortcut.additions.introducemock.Abcdef;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;

public abstract class BaseImpl implements Abcdef {

  private final Project project;
  private final PsiFile psiFile;

  public BaseImpl(Project project, PsiFile psiFile) {
    this.project = project;
    this.psiFile = psiFile;
  }

  public PsiClass getClassFromType(PsiType type) {
    return JavaPsiFacade.getInstance(project)
        .findClass(type.getCanonicalText(), GlobalSearchScope.fileScope(psiFile));
  }
}
