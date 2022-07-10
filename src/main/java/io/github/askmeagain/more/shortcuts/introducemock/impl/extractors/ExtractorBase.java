package io.github.askmeagain.more.shortcuts.introducemock.impl.extractors;

import io.github.askmeagain.more.shortcuts.introducemock.impl.IntroduceExtractors;
import com.intellij.openapi.project.Project;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class ExtractorBase implements IntroduceExtractors {

  protected final Project project;

}
