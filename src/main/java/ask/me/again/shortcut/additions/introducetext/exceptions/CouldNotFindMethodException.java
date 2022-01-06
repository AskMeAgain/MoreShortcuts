package ask.me.again.shortcut.additions.introducetext.exceptions;

import com.intellij.psi.PsiElement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CouldNotFindMethodException extends Exception {

    @Getter
    private final PsiElement cursorPosition;

}
