package ask.me.again.shortcut.additions.introducemock.impl;

import ask.me.again.shortcut.additions.introducemock.IntroduceMockImplementation;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;

public abstract class BaseImpl implements IntroduceMockImplementation {

  private final Project project;
  private final PsiFile psiFile;
  protected final StringBuilder stringBuilder;

  public BaseImpl(Project project, PsiFile psiFile, StringBuilder stringBuilder) {
    this.project = project;
    this.psiFile = psiFile;
    this.stringBuilder = stringBuilder;
  }

  public PsiClass getClassFromType(PsiType type) {
    return JavaPsiFacade.getInstance(project)
        .findClass(type.getCanonicalText(), GlobalSearchScope.fileScope(psiFile));
  }
}
