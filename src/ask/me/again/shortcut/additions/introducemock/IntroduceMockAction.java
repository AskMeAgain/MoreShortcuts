package ask.me.again.shortcut.additions.introducemock;

import ask.me.again.shortcut.additions.introducemock.entities.PsiHelpers;
import ask.me.again.shortcut.additions.introducemock.exceptions.ExpressionListNotFoundException;
import ask.me.again.shortcut.additions.introducemock.exceptions.MultipleResultException;
import ask.me.again.shortcut.additions.introducemock.exceptions.ExecutionTypeNotFoundException;
import ask.me.again.shortcut.additions.introducemock.exceptions.PsiTypeNotFoundException;
import ask.me.again.shortcut.additions.introducemock.impl.IntroduceMock;
import com.intellij.openapi.actionSystem.*;
import com.intellij.psi.*;

import java.io.PrintWriter;
import java.io.StringWriter;

public class IntroduceMockAction extends AnAction {

  public IntroduceMockAction() {
  }

  private PsiParameter[] override = new PsiParameter[0];

  public IntroduceMockAction(String name, PsiParameter[] override) {
    super(name);
    this.override = override;
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
      new IntroduceMock(actionEvent).runIntroduceMock(override);
    } catch (MultipleResultException multipleResultException) {

      var actionGroup = new DefaultActionGroup();

      multipleResultException.getPsiParametersList().forEach(parameterOverride -> {
        actionGroup.add(new IntroduceMockAction(getActionName(parameterOverride), parameterOverride));
      });

      var menu = ActionManager.getInstance().createActionPopupMenu("Filter", actionGroup);

      var editor = actionEvent.getRequiredData(CommonDataKeys.EDITOR);
      var contentComponent = editor.getContentComponent();

      var point = editor.logicalPositionToXY(editor.getCaretModel().getPrimaryCaret().getLogicalPosition());

      menu.getComponent().show(contentComponent, point.getLocation().x, point.getLocation().y + 30);
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
      PsiHelpers.print(actionEvent.getProject(), "Message: " + exception.getMessage() + "Stack trace: " + sw.toString());
    }
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
