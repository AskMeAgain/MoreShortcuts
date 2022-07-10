package io.github.askmeagain.more.shortcuts.introducetext.exceptions;

import com.intellij.psi.PsiElement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CouldNotFindMethodException extends Exception {

    @Getter
    private final PsiElement cursorPosition;

}
