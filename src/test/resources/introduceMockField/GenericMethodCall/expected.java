package IntroduceMockFieldTest.Abc;

import ask.me.again.shortcut.additions.introducemock.entities.TestClass;
import org.mockito.Mock;

class IntroduceGenericMockFieldExample {
    @Mock
    GenericTestClass<String, String> genericTestClass;

    void example() {
        var testClass = new TestClass();
        var result = testClass.testMethodGeneric(genericTestClass);
    }
}
