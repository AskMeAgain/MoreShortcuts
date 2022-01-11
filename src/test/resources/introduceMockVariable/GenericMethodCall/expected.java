package IntroduceMockFieldTest.Abc;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;
import org.mockito.Mockito;

class IntroduceGenericVariableExample {
    void example() {
        var testClass = new TestClass();
        var genericTestClass = Mockito.mock(GenericTestClass.class);

        var result = testClass.testMethodGeneric(genericTestClass);
    }
}