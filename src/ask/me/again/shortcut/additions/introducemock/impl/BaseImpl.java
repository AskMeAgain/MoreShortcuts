package ask.me.again.shortcut.additions.introducemock.impl;

import ask.me.again.shortcut.additions.introducemock.IntroduceMockImplementation;
import ask.me.again.shortcut.additions.introducemock.exceptions.ClassFromTypeNotFoundException;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;

import java.nio.charset.StandardCharsets;

public abstract class BaseImpl implements IntroduceMockImplementation {

  private final Project project;
  private final PsiFile psiFile;
  protected final StringBuilder stringBuilder;

  public BaseImpl(Project project, PsiFile psiFile, StringBuilder stringBuilder) {
    this.project = project;
    this.psiFile = psiFile;
    this.stringBuilder = stringBuilder;
  }

  public PsiClass getClassFromString(String name) throws ClassFromTypeNotFoundException {

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
