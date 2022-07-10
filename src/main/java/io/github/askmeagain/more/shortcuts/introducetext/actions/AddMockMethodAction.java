package io.github.askmeagain.more.shortcuts.introducetext.actions;

import io.github.askmeagain.more.shortcuts.introducetext.entities.IntroduceTextMode;
import io.github.askmeagain.more.shortcuts.introducetext.impl.AddMockMethodImpl;

public class AddMockMethodAction extends AddMockMethodImpl {
    public AddMockMethodAction() {
        super(IntroduceTextMode.MOCK_METHOD);
    }
}
