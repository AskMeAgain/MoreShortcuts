package ask.me.again.shortcut.additions.introducemock.impl.extractors;

import ask.me.again.shortcut.additions.introducemock.exceptions.ClassFromTypeNotFoundException;
import ask.me.again.shortcut.additions.introducemock.impl.IntroduceExtractors;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;

public abstract class ExtractorBase implements IntroduceExtractors {

  protected final Project project;

  public ExtractorBase(Project project) {
    this.project = project;
  }
}
