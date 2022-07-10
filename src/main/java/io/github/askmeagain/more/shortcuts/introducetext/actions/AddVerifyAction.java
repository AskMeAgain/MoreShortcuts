package io.github.askmeagain.more.shortcuts.introducetext.actions;

import io.github.askmeagain.more.shortcuts.introducetext.actions.entities.IntroduceTextMode;
import io.github.askmeagain.more.shortcuts.introducetext.impl.AddMockMethodImpl;

public class AddVerifyAction extends AddMockMethodImpl {
    public AddVerifyAction(){
        super(IntroduceTextMode.VERIFY);
    }
}
