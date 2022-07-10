package IntroduceMockFieldTest.Abc;

import io.github.askmeagain.more.shortcuts.introducemock.entities.TestClass;
import org.mockito.Mock;

class IntroduceGenericMockFieldExample {
    @Mock
    GenericTestClass<String, String> genericTestClass;

    void example() {
        var testClass = new TestClass();
        var result = testClass.testMethodGeneric(genericTestClass);
    }
}
