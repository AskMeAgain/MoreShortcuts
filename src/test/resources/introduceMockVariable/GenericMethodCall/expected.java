package IntroduceMockFieldTest.Abc;

import io.github.askmeagain.more.shortcuts.introducemock.entities.TestClass;
import org.mockito.Mockito;

class IntroduceGenericVariableExample {
    void example() {
        var testClass = new TestClass();
        var genericTestClass = Mockito.mock(GenericTestClass.class);

        var result = testClass.testMethodGeneric(genericTestClass);
    }
}