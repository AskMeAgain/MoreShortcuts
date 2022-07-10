package io.github.askmeagain.more.shortcuts.introducemock.entities;


public class TestClass {

  public void testMethodGeneric(GenericTestClass<String,String> generic){

  }

  public static String staticMethod(int i, String isa) {
    return null;
  }

  public static String staticMethod2(Integer i, String isa) {
    return null;
  }

  public static String sameParameterName(String i, String isa) {
    return null;
  }

  public TestClass() {
  }

  public TestClass(String a, String b) {
  }

  public void exampleMethod(Integer a, String d, Integer e) {

  }

  public void exampleMethod(String a, String d, Integer e) {

  }

  public String exampleMethod(int a, String d, double e) {
    return null;
  }

  public TestClass nested(Integer a, String b) {
    return this;
  }

  public TestClass context(Integer a, String b) {
    return this;
  }

  public TestClass context(String a, String b) {
    return this;
  }

  public TestClass(TestClass a, TestClass b, Integer c) {
  }

  public TestClass(Integer a, TestClass b, Integer c) {
  }
}


