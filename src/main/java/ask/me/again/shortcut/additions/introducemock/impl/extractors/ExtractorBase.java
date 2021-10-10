package ask.me.again.shortcut.additions.introducemock.impl.extractors;

import ask.me.again.shortcut.additions.introducemock.exceptions.ClassFromTypeNotFoundException;
import ask.me.again.shortcut.additions.introducemock.impl.IntroduceExtractors;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;

public abstract class ExtractorBase implements IntroduceExtractors {

  private final Project project;

  public ExtractorBase(Project project) {
    this.project = project;
  }

  public PsiClass getClassFromString(String name) throws ClassFromTypeNotFoundException {

    //substring until generic -> (char)60 == '<'
    int beginIndex = name.indexOf(60);

    if (beginIndex > -1) {
      name = name.substring(0, beginIndex);
    }

    var result = JavaPsiFacade.getInstance(project)
        .findClass(name, GlobalSearchScope.allScope(project));

    if (result == null) {
      var equals = name.equals("ask.me.again.meshinery.core.common.MeshineryTask");
      throw new ClassFromTypeNotFoundException("FOUND Nothing, are equal?" + equals);

    }
    return result;
  }
}
