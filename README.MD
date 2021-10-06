# Intellij Shortcut Additions

This plugin adds some shortcuts to intellij idea

## Installation

It is currently not in the intellij plugin marketplace, but you can install it manually

1. Settings
2. Plugins
3. Settings Cog (top right)
4. Install Plugin from disk
5. Select jar

----------------------

## Introduce Mock

Introduces Mockito mocks for every "null" value of the class. Place cursor inside the brackets and run the "Introduce
Mock"
action. Its also smart enough, that if a conflict arises it asks for the correct implementation via context menu

Before:

```java
    var classToMock = new ClassToMock(null,null);
```

After:

```java
    var class1 = Mockito.mock(Class1.class);
    var anotherClass = Mockito.mock(AnotherClass.class);

    var classToMock = new ClassToMock(class1, anotherClass, "This is ignored as its not null");
```
