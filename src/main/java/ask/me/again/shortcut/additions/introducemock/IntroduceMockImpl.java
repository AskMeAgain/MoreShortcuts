package ask.me.again.shortcut.additions.introducemock;

import ask.me.again.shortcut.additions.PsiHelpers;
import ask.me.again.shortcut.additions.introducemock.entities.ExecutionTarget;
import ask.me.again.shortcut.additions.introducemock.exceptions.*;
import ask.me.again.shortcut.additions.introducemock.impl.IntroduceMock;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.PsiParameter;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.io.StringWriter;

import static com.intellij.openapi.ui.popup.JBPopupFactory.ActionSelectionAid.SPEEDSEARCH;

@NoArgsConstructor
public class IntroduceMockImpl extends AnAction {

  private ExecutionTarget executionTarget;
  private PsiParameter[] override = new PsiParameter[0];

  protected IntroduceMockImpl(ExecutionTarget executionTarget) {
    this.executionTarget = executionTarget;
  }

  protected IntroduceMockImpl(String name, PsiParameter[] override, ExecutionTarget executionTarget) {
    super(name);
    this.override = override;
    this.executionTarget = executionTarget;
  }

  @Override
  public void update(AnActionEvent e) {
    var presentation = e.getPresentation();
    presentation.setVisible(true);
    presentation.setEnabled(e.getData(CommonDataKeys.EDITOR) != null);
  }

  @Override
  public void actionPerformed(AnActionEvent actionEvent) {

    var project = actionEvent.getProject();
    var introduceMock = new IntroduceMock(actionEvent, executionTarget);

    ApplicationManager.getApplication().runReadAction(() -> {
      try {
        introduceMock.analyze(override);
      } catch (MultipleIntroduceMockResultException multipleResultException) {
        createContextMenu(actionEvent, multipleResultException);
      } catch (ExpressionListNotFoundException elnfe) {
        PsiHelpers.print(project, "Could not find expression list :(");
      } catch (ExecutionTypeNotFoundException etnfe) {
        PsiHelpers.print(project, "Could not get the refactoring type out of the current block :(");
      } catch (PsiTypeNotFoundException ptnfe) {
        PsiHelpers.print(project, "Could not find psi type :(");
      } catch (UnsupportedOperationException exception) {
        PsiHelpers.print(project, "Message: " + exception.getMessage());
      } catch (Exception exception) {
        var sw = new StringWriter();
        var pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        PsiHelpers.print(project, "Message: " + exception.getMessage() + "\nStack trace: " + sw);
      }
    });

    WriteCommandAction.runWriteCommandAction(project, () -> {
      try {
        introduceMock.refactorCode();

      } catch (ClassFromTypeNotFoundException e) {
        PsiHelpers.print(project, "ClassFromTypeNotFoundException :(");
      }
    });
  }

  private void createContextMenu(AnActionEvent actionEvent, MultipleIntroduceMockResultException multipleResultException) {
    var actionGroup = new DefaultActionGroup();

    multipleResultException.getPsiParametersList()
        .forEach(parameterOverride -> actionGroup.add(new IntroduceMockImpl(getActionName(parameterOverride), parameterOverride, executionTarget)));

    var editor = actionEvent.getRequiredData(CommonDataKeys.EDITOR);

    var popup = JBPopupFactory.getInstance()
        .createActionGroupPopup(null, actionGroup, actionEvent.getDataContext(), SPEEDSEARCH, false);

    popup.showInBestPositionFor(editor);
  }

  private String getActionName(PsiParameter[] psiParameters) {

    var stringBuilder = new StringBuilder();

    for (var psiParameter : psiParameters) {
      var type = psiParameter.getType();
      stringBuilder.append(type.getPresentableText() + " ");
    }

    return stringBuilder.toString();
  }
}
