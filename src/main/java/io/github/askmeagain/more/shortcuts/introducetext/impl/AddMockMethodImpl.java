package io.github.askmeagain.more.shortcuts.introducetext.impl;

import com.intellij.psi.impl.PsiParserFacadeImpl;
import io.github.askmeagain.more.shortcuts.commons.PsiHelpers;
import io.github.askmeagain.more.shortcuts.commons.CommonPsiUtils;
import io.github.askmeagain.more.shortcuts.introducemock.exceptions.ClassFromTypeNotFoundException;
import io.github.askmeagain.more.shortcuts.introducetext.actions.entities.IntroduceTextMode;
import io.github.askmeagain.more.shortcuts.introducetext.exceptions.CouldNotFindMethodException;
import io.github.askmeagain.more.shortcuts.introducetext.exceptions.MultipleAddMockMethodResultException;
import io.github.askmeagain.more.shortcuts.settings.MoreShortcutState;
import io.github.askmeagain.more.shortcuts.settings.PersistenceManagementService;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.jgoodies.common.base.Strings;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.intellij.openapi.ui.popup.JBPopupFactory.ActionSelectionAid.SPEEDSEARCH;

@NoArgsConstructor
public class AddMockMethodImpl extends AnAction {

  private PsiIdentifier identifier;
  private PsiMethod override;
  private Editor editor;
  private PsiFile psiFile;
  private Project project;
  private PsiElementFactory factory;
  private IntroduceTextMode introduceTextMode;

  private final MoreShortcutState state = PersistenceManagementService.getInstance().getState();

  public AddMockMethodImpl(IntroduceTextMode introduceTextMode) {
    this.introduceTextMode = introduceTextMode;
  }

  public AddMockMethodImpl(PsiMethod override, PsiIdentifier identifier, IntroduceTextMode introduceTextMode) {
    super(override == null ? identifier.getText() : override.getName());
    this.override = override;
    this.identifier = identifier;
    this.introduceTextMode = introduceTextMode;
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
      var cursorPosition = getCursorElement();
      var cursorElement = identifier != null ? identifier : cursorPosition;

      var method = override != null ? override : findMethod(cursorElement);

      var methodParameters = createParameters(method);
      var resultText = getResultText(cursorElement, method, methodParameters);

      PsiElement expression = factory.createStatementFromText(resultText, null);

      writeExpression(cursorPosition, expression);
      writeImportStatements(method);

    } catch (CouldNotFindMethodException ex) {
      createContextMenuForVariable(e, ex);
    } catch (ClassFromTypeNotFoundException ex) {
      PsiHelpers.print(project, "ClassFromTypeNotFoundException!");
    } catch (MultipleAddMockMethodResultException ex) {
      createContextMenu(e, ex);
    }
  }

  private String getResultText(PsiElement cursorElement, PsiMethod method, String parameters) {
    var template = introduceTextMode == IntroduceTextMode.MOCK_METHOD
        ? "Mockito.when(%s.%s(%s)).thenReturn(null);"
        : "Mockito.verify(%s).%s(%s);";
    var codeText = String.format(template,
        cursorElement.getText(),
        method.getName(),
        parameters);

    if (state.getStaticImports()) {
      codeText = codeText.replaceFirst("Mockito\\.", "");
    }

    return codeText;
  }

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

    if (state.getIntroduceMockFieldPrivateField()) {
      CommonPsiUtils.addStaticImports(psiFile, project, "verify", "when");
    }
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
      var whiteSpace = new PsiParserFacadeImpl(project).createWhiteSpaceFromText("\n");

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

  private void createContextMenu(AnActionEvent actionEvent, MultipleAddMockMethodResultException multipleResultException) {
    var actionGroup = new DefaultActionGroup();

    multipleResultException.getPsiMethodList()
        .forEach(parameterOverride -> actionGroup.add(new AddMockMethodImpl(parameterOverride, identifier, introduceTextMode)));

    var editor = actionEvent.getRequiredData(CommonDataKeys.EDITOR);
    var popup = JBPopupFactory.getInstance()
        .createActionGroupPopup(null, actionGroup, actionEvent.getDataContext(), SPEEDSEARCH, false);
    popup.showInBestPositionFor(editor);
  }

  private void createContextMenuForVariable(AnActionEvent actionEvent, CouldNotFindMethodException couldNotFindMethodException) {
    var actionGroup = new DefaultActionGroup();

    var findAllStuff = PsiHelpers.findAllRecursivelyInBlock(couldNotFindMethodException.getCursorPosition());

    findAllStuff.forEach(psiIdentifier -> actionGroup.add(new AddMockMethodImpl(null, psiIdentifier, introduceTextMode)));

    var editor = actionEvent.getRequiredData(CommonDataKeys.EDITOR);
    var popup = JBPopupFactory.getInstance()
        .createActionGroupPopup(null, actionGroup, actionEvent.getDataContext(), SPEEDSEARCH, false);
    popup.showInBestPositionFor(editor);
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

    var parameter = PsiTreeUtil.getParentOfType(element, PsiParameter.class);
    if (parameter != null) {
      typeString = parameter.getType().getCanonicalText();
    }

    var fieldParam = PsiTreeUtil.getParentOfType(element, PsiField.class);
    if (fieldParam != null) {
      typeString = fieldParam.getType().getCanonicalText();
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

    throw new CouldNotFindMethodException(element);
  }
}
