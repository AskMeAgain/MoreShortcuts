package ask.me.again.shortcut.additions.introducemock;

import ask.me.again.shortcut.additions.introducemock.helpers.ExecutionType;
import ask.me.again.shortcut.additions.introducemock.impl.ConstructorImpl;
import ask.me.again.shortcut.additions.introducemock.impl.MethodImpl;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import jnr.ffi.Struct;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

import static ask.me.again.shortcut.additions.introducemock.PsiHelpers.decapitalizeString;
import static com.intellij.build.BuildContentManager.TOOL_WINDOW_ID;

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
  public void actionPerformed(AnActionEvent e) {

    var introduceMock = new IntroduceMock(e);

    try {
      introduceMock.runIntroduceMock(override);
    } catch (MultipleResultException multipleResultException) {

      var actionGroup = new DefaultActionGroup();

      multipleResultException.getPsiParametersList().forEach(parameterOverride -> {
        actionGroup.add(new IntroduceMockAction(getActionName(parameterOverride), parameterOverride));
      });

      var menu = ActionManager.getInstance().createActionPopupMenu("Filter", actionGroup);

      var editor = e.getRequiredData(CommonDataKeys.EDITOR);
      var contentComponent = editor.getContentComponent();

      var point = editor.logicalPositionToXY(editor.getCaretModel().getPrimaryCaret().getLogicalPosition());

      menu.getComponent().show(contentComponent, point.getLocation().x, point.getLocation().y + 30);
    }
  }

  private String getActionName(PsiParameter[] x) {

    var stringBuilder = new StringBuilder();

    for (int i = 0; i < x.length; i++) {
      var type = x[i].getType();
      stringBuilder.append(type.getPresentableText() + " ");
    }

    return stringBuilder.toString();
  }
}
