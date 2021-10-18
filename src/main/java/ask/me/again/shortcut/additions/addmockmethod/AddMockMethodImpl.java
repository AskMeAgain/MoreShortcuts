package ask.me.again.shortcut.additions.addmockmethod;

import ask.me.again.shortcut.additions.PsiHelpers;
import ask.me.again.shortcut.additions.addmockmethod.exceptions.CouldNotFindAnchorException;
import ask.me.again.shortcut.additions.addmockmethod.exceptions.CouldNotFindMethodException;
import ask.me.again.shortcut.additions.addmockmethod.exceptions.MultipleAddMockMethodResultException;
import ask.me.again.shortcut.additions.introducemock.exceptions.ClassFromTypeNotFoundException;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.jgoodies.common.base.Strings;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AddMockMethodImpl extends AnAction {

  private PsiMethod override;
  private Editor editor;
  private PsiFile psiFile;
  private Project project;
  private PsiElementFactory factory;

  public AddMockMethodImpl() {
  }

  public AddMockMethodImpl(PsiMethod override) {
    super(override.getName());
    this.override = override;
  }

  @Override
  public void update(AnActionEvent e) {
    var presentation = e.getPresentation();
    presentation.setVisible(true);
    presentation.setEnabled(e.getData(CommonDataKeys.EDITOR) != null);
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    editor = e.getData(CommonDataKeys.EDITOR);
    project = e.getData(CommonDataKeys.PROJECT);
    psiFile = e.getData(CommonDataKeys.PSI_FILE);
    factory = JavaPsiFacade.getElementFactory(project);

    try {
      var cursorElement = getCursorElement();

      var method = override != null ? override : findMethod(cursorElement);

      var resultText = String.format("Mockito.when(%s.%s(%s)).thenReturn(null);",
          cursorElement.getText(),
          method.getName(),
          createParameters(method));

      PsiElement expression = factory.createStatementFromText(resultText, null);
      var anchor = findAnchor(cursorElement);

      writeExpression(anchor, expression);
      writeImportStatements(method);

    } catch (CouldNotFindMethodException ex) {
      PsiHelpers.print(project, "Could not find method exception!");
    } catch (ClassFromTypeNotFoundException ex) {
      PsiHelpers.print(project, "ClassFromTypeNotFoundException!");
    } catch (CouldNotFindAnchorException couldNotFindAnchorException) {
      PsiHelpers.print(project, "couldNotFindAnchorException!");
    } catch (MultipleAddMockMethodResultException ex) {
      createContextMenu(e, ex);
    }
  }

  @Nullable
  private PsiElement getCursorElement() {
    var simpleCursor = psiFile.findElementAt(editor.getCaretModel().getOffset());

    if (simpleCursor != null) {
      return simpleCursor;
    }

    throw new NotImplementedException("TODO");
  }

  private void writeImportStatements(PsiMethod method) throws ClassFromTypeNotFoundException {
    var imports = List.of(PsiHelpers.getClassFromString(project, "org.mockito.Mockito"));

    var set = Arrays.stream(method.getParameterList().getParameters())
        .map(PsiParameter::getType)
        .map(this::mapToString)
        .map(x -> x.substring(0, x.length()))
        .collect(Collectors.toSet());

    var classFromString = PsiHelpers.getClassFromString(project, "org.mockito.ArgumentMatchers");

    WriteCommandAction.runWriteCommandAction(project, () -> {
      var importList = PsiTreeUtil.getChildOfType(psiFile, PsiImportList.class);
      if (importList != null) {
        imports.forEach(x -> importList.add(factory.createImportStatement(x)));

        for (var obj : set) {
          importList.add(factory.createImportStaticStatement(classFromString, obj.substring(0, obj.length() - 2)));
        }
      }
    });
  }


  private String createParameters(PsiMethod method) {
    return Arrays.stream(method.getParameterList().getParameters())
        .map(PsiParameter::getType)
        .map(this::mapToString)
        .collect(Collectors.joining(", "));
  }

  private String mapToString(PsiType psiType) {
    if (psiType.equalsToText("int")) {
      return "anyInt()";
    } else if (psiType.equalsToText("boolean")) {
      return "anyBoolean()";
    } else if (psiType.equalsToText("double")) {
      return "anyDouble()";
    } else {
      return "any()";
    }
  }

  private void writeExpression(PsiElement anchor, PsiElement expression) {
    WriteCommandAction.runWriteCommandAction(project, () -> {
      var whiteSpace = PsiParserFacade.SERVICE.getInstance(project).createWhiteSpaceFromText("\n");

      var codeBlock = anchor.getParent();

      var result = codeBlock.addAfter(expression, anchor);
      codeBlock.addAfter(whiteSpace, anchor);

      new ReformatCodeProcessor(psiFile, true).run();

      var methodCall = PsiTreeUtil.getChildOfType(result, PsiMethodCallExpression.class);
      var expressionList = PsiTreeUtil.getChildOfType(methodCall, PsiExpressionList.class);

      var offset = expressionList.getTextOffset() + 1;
      editor.getCaretModel().moveToOffset(offset);
      editor.getSelectionModel().setSelection(offset, offset + 4);
    });
  }

  private PsiElement findAnchor(PsiElement current) throws CouldNotFindAnchorException {
    for (int i = 0; i < 20; i++) {
      if (current.getParent() instanceof PsiCodeBlock) {
        return current;
      }
      current = current.getParent();
    }

    throw new CouldNotFindAnchorException();
  }

  private void createContextMenu(AnActionEvent actionEvent, MultipleAddMockMethodResultException multipleResultException) {
    var actionGroup = new DefaultActionGroup();

    multipleResultException.getPsiMethodList()
        .forEach(parameterOverride -> actionGroup.add(new AddMockMethodImpl(parameterOverride)));

    var menu = ActionManager.getInstance().createActionPopupMenu("Method", actionGroup);

    var editor = actionEvent.getRequiredData(CommonDataKeys.EDITOR);
    var contentComponent = editor.getContentComponent();

    var point = editor.logicalPositionToXY(editor.getCaretModel().getPrimaryCaret().getLogicalPosition());
    menu.getComponent().show(contentComponent, point.getLocation().x, point.getLocation().y + 30);
  }

  private PsiMethod findMethod(PsiElement element) throws CouldNotFindMethodException, ClassFromTypeNotFoundException, MultipleAddMockMethodResultException {

    var parent = PsiTreeUtil.getParentOfType(element, PsiReferenceExpression.class);
    var typeString = "";
    if (parent != null) {
      typeString = parent.getType().getCanonicalText();
    }

    var localVar = PsiTreeUtil.getParentOfType(element, PsiLocalVariable.class);
    if (localVar != null) {
      typeString = localVar.getType().getCanonicalText();
    }

    if (!Strings.isBlank(typeString)) {
      var psiClass = PsiHelpers.getClassFromString(project, typeString);

      var resultList = Arrays.stream(psiClass.getAllMethods())
          .filter(x -> x.getReturnType() != null)
          .filter(x -> !x.getReturnType().equalsToText("void"))
          .filter(x -> !x.hasModifierProperty(PsiModifier.PRIVATE))
          .filter(x -> !x.hasModifierProperty(PsiModifier.STATIC))
          .collect(Collectors.toList());

      if (resultList.size() == 1) {
        return resultList.get(0);
      }

      throw new MultipleAddMockMethodResultException(resultList);
    }

    throw new CouldNotFindMethodException();
  }
}
