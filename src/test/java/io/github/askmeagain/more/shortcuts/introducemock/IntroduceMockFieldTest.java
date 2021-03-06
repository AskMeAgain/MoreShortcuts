package io.github.askmeagain.more.shortcuts.introducemock;

import io.github.askmeagain.more.shortcuts.introducemock.utils.SimpleRefactoringTestBase;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInfo;

public class IntroduceMockFieldTest extends SimpleRefactoringTestBase {

  @Override
  public String getAction() {
    return "ask.me.again.shortcut.additions.introducemock.field";
  }

  @TestFactory
  public Iterable<DynamicTest> introduceMockField(TestInfo testInfo) {
    return dynamicTestsWithIterable(testInfo,
        "ConstructorMethodCall", "",
        "VariableMethodCall", "",
        "FieldMethodCall", "",
        "StaticMethodCall1", "",
        "StaticMethodCall2", "",
        "NonNullParameter1", "",
        "NonNullParameter2", "",
        "ConstructorNoResultMethodCall", "",
        "FieldNoResultMethodCall", "",
        "BuilderVariableMethodCall", "",
        "BuilderVariableNoResultMethodCall", "",
        "BuilderFieldNoResultMethodCall", "",
        "BuilderFieldMethodCall", "",
        "ConstructorCall", "",
        "NoResultConstructorCall", "",
        "SameParameterName", "",
        "GenericMethodCall", ""
    );
  }
}
