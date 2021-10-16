package ask.me.again.shortcut.additions.introducemock;

import ask.me.again.shortcut.additions.PsiHelpers;
import ask.me.again.shortcut.additions.introducemock.exceptions.ExpressionListNotFoundException;
import ask.me.again.shortcut.additions.introducemock.exceptions.MultipleIntroduceMockResultException;
import ask.me.again.shortcut.additions.introducemock.exceptions.ExecutionTypeNotFoundException;
import ask.me.again.shortcut.additions.introducemock.exceptions.PsiTypeNotFoundException;
import ask.me.again.shortcut.additions.introducemock.impl.IntroduceMock;
import ask.me.again.shortcut.additions.introducemock.entities.ExecutionTarget;
import com.intellij.openapi.actionSystem.*;
import com.intellij.psi.*;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.io.StringWriter;

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

    try {
      new IntroduceMock(actionEvent).runIntroduceMock(override, executionTarget);
    } catch (MultipleIntroduceMockResultException multipleResultException) {
      createContextMenu(actionEvent, multipleResultException);
    } catch (ExecutionTypeNotFoundException etnfe) {
      PsiHelpers.print(actionEvent.getProject(), "Could not get the refactoring type out of the current block :(");
    } catch (PsiTypeNotFoundException ptnfe) {
      PsiHelpers.print(actionEvent.getProject(), "Could not find psi type :(");
    } catch (ExpressionListNotFoundException elnfe) {
      PsiHelpers.print(actionEvent.getProject(), "Could not find expression list :(");
    } catch (Exception exception) {
      var sw = new StringWriter();
      var pw = new PrintWriter(sw);
      exception.printStackTrace(pw);
      PsiHelpers.print(actionEvent.getProject(), "Message: " + exception.getMessage() + "\nStack trace: " + sw);
    }
  }

  private void createContextMenu(AnActionEvent actionEvent, MultipleIntroduceMockResultException multipleResultException) {
    var actionGroup = new DefaultActionGroup();

    multipleResultException.getPsiParametersList().forEach(parameterOverride -> {
      actionGroup.add(new IntroduceMockImpl(getActionName(parameterOverride), parameterOverride, executionTarget));
    });

    var menu = ActionManager.getInstance().createActionPopupMenu("Filter", actionGroup);

    var editor = actionEvent.getRequiredData(CommonDataKeys.EDITOR);
    var contentComponent = editor.getContentComponent();

    var point = editor.logicalPositionToXY(editor.getCaretModel().getPrimaryCaret().getLogicalPosition());
    menu.getComponent().show(contentComponent, point.getLocation().x, point.getLocation().y + 30);
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
