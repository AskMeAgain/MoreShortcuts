package ask.me.again.shortcut.additions.mapstructbuilder;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClassOwner;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static ask.me.again.shortcut.additions.mapstructbuilder.MapStructBuilderTemplate.MAPPING_TEMPLATE;
import static ask.me.again.shortcut.additions.mapstructbuilder.MapStructBuilderTemplate.TEMPLATE;

public class MapStructBuilderAction extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {

    var editor = e.getRequiredData(CommonDataKeys.EDITOR);
    var document = editor.getDocument();
    var project = e.getProject();
    var psiFile = e.getData(CommonDataKeys.PSI_FILE);
    var data = e.getData(PlatformDataKeys.VIRTUAL_FILE);

    editor.getCaretModel().runForEachCaret(caret -> {
      var start = caret.getSelectionStart();
      var end = caret.getSelectionEnd();
      var text = document.getText(TextRange.from(start, end - start));

      var split = text.split("\n");
      var mappings = getMappings(split);

      var outputType = MapStructBuilderUtils.findOutputType(split[0]);

      var packageName = ((PsiClassOwner)psiFile).getPackageName();

      var result = TEMPLATE
          .replace("$PACKAGE", packageName)
          .replace("$OUTPUT_TYPE", outputType)
          .replace("$MAPPINGS", String.join("\n", mappings));

      WriteCommandAction.runWriteCommandAction(project, () -> {
        try {
          var resultFile = data.getParent().createChildData(null, outputType + "Mapper.java");
          resultFile.setBinaryContent(result.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      });
    });
  }

  @NotNull
  private ArrayList<String> getMappings(String[] split) {
    var mappings = new ArrayList<String>();
    for (int i = 1; i < split.length - 1; i++) {
      var mappingLine = split[i];

      var target = MapStructBuilderUtils.findTarget(mappingLine);
      var source = MapStructBuilderUtils.findSource(mappingLine);

      var template = MAPPING_TEMPLATE
          .replace("$OUTPUT_NAME", target)
          .replace("$INPUT_NAME", source);
      mappings.add(template);
    }
    return mappings;
  }
}
