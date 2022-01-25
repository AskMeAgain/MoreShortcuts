package ask.me.again.shortcut.additions.multilinemagic;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MultilineUtils {
  public static void extracted(@NotNull AnActionEvent e, int startCount, int multiplier) {
    var editor = e.getRequiredData(CommonDataKeys.EDITOR);
    var document = editor.getDocument();

    var project = e.getProject();

    var counter = new AtomicInteger();

    WriteCommandAction.runWriteCommandAction(project, () -> {
      editor.getCaretModel().runForEachCaret(caret -> {
        var number = String.valueOf(startCount + counter.getAndIncrement() * multiplier);
        document.insertString(caret.getOffset(), number);
      });
    });
  }

  public static void extractedChar(@NotNull AnActionEvent e, char startChar, int multiplier) {
    var editor = e.getRequiredData(CommonDataKeys.EDITOR);
    var document = editor.getDocument();

    var project = e.getProject();

    var counter = new AtomicInteger();
    var startCount = (int) startChar;

    WriteCommandAction.runWriteCommandAction(project, () -> {
      editor.getCaretModel().runForEachCaret(caret -> {
        var number = String.valueOf(getChar(multiplier, counter, startCount));
        document.insertString(caret.getOffset(), number);
      });
    });
  }

  private static char getChar(int multiplier, AtomicInteger counter, int startCount) {
    return (char) (startCount + counter.getAndIncrement() * multiplier);
  }

  /**
   * http://docs.oracle.com/javase/tutorial/uiswing/examples/layout/SpringGridProject/src/layout/SpringUtilities.java
   */
  public static void makeCompactGrid(Container parent,
                                     int rows, int cols,
                                     int initialX, int initialY,
                                     int xPad, int yPad) {
    SpringLayout layout;
    try {
      layout = (SpringLayout)parent.getLayout();
    } catch (ClassCastException exc) {
      System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
      return;
    }

    //Align all cells in each column and make them the same width.
    Spring x = Spring.constant(initialX);
    for (int c = 0; c < cols; c++) {
      Spring width = Spring.constant(0);
      for (int r = 0; r < rows; r++) {
        width = Spring.max(width,
            getConstraintsForCell(r, c, parent, cols).
                getWidth());
      }
      for (int r = 0; r < rows; r++) {
        SpringLayout.Constraints constraints =
            getConstraintsForCell(r, c, parent, cols);
        constraints.setX(x);
        constraints.setWidth(width);
      }
      x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
    }

    //Align all cells in each row and make them the same height.
    Spring y = Spring.constant(initialY);
    for (int r = 0; r < rows; r++) {
      Spring height = Spring.constant(0);
      for (int c = 0; c < cols; c++) {
        height = Spring.max(height,
            getConstraintsForCell(r, c, parent, cols).
                getHeight());
      }
      for (int c = 0; c < cols; c++) {
        SpringLayout.Constraints constraints =
            getConstraintsForCell(r, c, parent, cols);
        constraints.setY(y);
        constraints.setHeight(height);
      }
      y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
    }

    //Set the parent's size.
    SpringLayout.Constraints pCons = layout.getConstraints(parent);
    pCons.setConstraint(SpringLayout.SOUTH, y);
    pCons.setConstraint(SpringLayout.EAST, x);
  }

  /* Used by makeCompactGrid. */
  private static SpringLayout.Constraints getConstraintsForCell(
      int row, int col,
      Container parent,
      int cols) {
    SpringLayout layout = (SpringLayout) parent.getLayout();
    Component c = parent.getComponent(row * cols + col);
    return layout.getConstraints(c);
  }
}
