package io.github.askmeagain.more.shortcuts.introducemock;

import io.github.askmeagain.more.shortcuts.introducemock.utils.SimpleRefactoringTestBase;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInfo;

public class IntroduceMockVariableTest extends SimpleRefactoringTestBase {

  @Override
  public String getAction() {
    return "io.github.askmeagain.more.shortcuts.introducemock.actions.IntroduceMockToVariable";
  }

  @TestFactory
  public Iterable<DynamicTest> introduceMockVariable(TestInfo testInfo) {
    return dynamicTestsWithIterable(testInfo,
        "ConstructorMethodCall","",
        "ConstructorNoResultMethodCall","",
        "VariableMethodCall","",
        "FieldMethodCall","",
        "StaticMethodCall1","",
        "StaticMethodCall2","",
        "NonNullParameter1","",
        "NonNullParameter2","",
        "FieldNoResultMethodCall","",
        "BuilderVariableMethodCall","",
        "BuilderVariableNoResultMethodCall","",
        "BuilderFieldNoResultMethodCall","",
        "BuilderFieldMethodCall","",
        "ConstructorCall","",
        "SameParameterName","",
        "NoResultConstructorCall","",
        "GenericMethodCall",""
    );
  }
}
