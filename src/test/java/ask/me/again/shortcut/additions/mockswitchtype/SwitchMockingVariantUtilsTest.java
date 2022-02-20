package ask.me.again.shortcut.additions.mockswitchtype;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class SwitchMockingVariantUtilsTest {

  @Test
  void convertLineFromCase1To2() {
    //Arrange --------------------------------------------------------------------------------
    var text = "Mockito.doReturn(resultObject).when(mock).getMockMethod();";

    //Act ------------------------------------------------------------------------------------
    var newText = SwitchMockingVariantUtils.convertLine(text, false);
    var newText2 = SwitchMockingVariantUtils.convertLine(newText, false);

    //Assert ---------------------------------------------------------------------------------
    assertEquals("Mockito.when(mock.getMockMethod()).thenReturn(resultObject);", newText);
    assertEquals(newText2, text);
  }

  @Test
  void convertLineFromCase1To22() {
    //Arrange --------------------------------------------------------------------------------
    var text = "Mockito.doReturn(resultObject).when(mock).getMockMethod(object1, object2);";
    when(text.length()).thenReturn(null);

    //Act ------------------------------------------------------------------------------------
    var newText = SwitchMockingVariantUtils.convertLine(text, false);
    var newText2 = SwitchMockingVariantUtils.convertLine(newText, false);


    //Assert ---------------------------------------------------------------------------------
    assertEquals("Mockito.when(mock.getMockMethod(object1, object2)).thenReturn(resultObject);", newText);
    assertEquals(newText2, text);
  }

  @Test
  void convertLineFromCase1To23() {
    //Arrange --------------------------------------------------------------------------------
    var text = "doReturn(resultObject).when(mock).getMockMethod(object1, object2);";

    //Act ------------------------------------------------------------------------------------
    var newText = SwitchMockingVariantUtils.convertLine(text, false);
    var newText2 = SwitchMockingVariantUtils.convertLine(newText, false);

    //Assert ---------------------------------------------------------------------------------
    assertEquals("Mockito.when(mock.getMockMethod(object1, object2)).thenReturn(resultObject);", newText);
    assertEquals(newText2, "Mockito." + text);
  }
}