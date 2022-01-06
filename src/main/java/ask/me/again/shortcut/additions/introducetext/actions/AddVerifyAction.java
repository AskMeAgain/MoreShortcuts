package ask.me.again.shortcut.additions.introducetext.actions;

import ask.me.again.shortcut.additions.introducetext.entities.IntroduceTextMode;
import ask.me.again.shortcut.additions.introducetext.impl.AddMockMethodImpl;

public class AddVerifyAction extends AddMockMethodImpl {
    public AddVerifyAction(){
        super(IntroduceTextMode.VERIFY);
    }
}
