package ask.me.again.shortcut.additions.introducemock.impl.extractors;

import ask.me.again.shortcut.additions.introducemock.impl.IntroduceExtractors;
import com.intellij.openapi.project.Project;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class ExtractorBase implements IntroduceExtractors {

  protected final Project project;

}
