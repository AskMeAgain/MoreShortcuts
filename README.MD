# MoreShortcuts for Intellij

This plugin adds more shortcuts to intellij idea.

## Installation

Get it from the [Intellij Marketplace](https://plugins.jetbrains.com/plugin/17814-more-shortcuts/)

----------------------

## Introduce Mock To Variable

Introduces Mockito mocks for every "null" value of the class. Place cursor inside the brackets and run the "Introduce
Mock To Variable" action (**CTRL + M, CTRL + V**). In case of a conflict a context menu pops up to resolve it.

Before

    void example() {
        var result = new TestClass(null, null);
    }

After

    void example() {
        var string1 = Mockito.mock(String.class);
        var string2 = Mockito.mock(String.class);

        var result = new TestClass(string1, string2);
    }

PS: you can also introduce spys like this, just use **CTRL + SHIFT + M, CTRL + V** instead.

----------------------

## Introduce Mock To Field

Introduces Mockito mock fields for every "null" value of the class. Place cursor inside the brackets and run the "
Introduce Mock To Field" action (**CTRL + M, CTRL + F**). Its also smart enough, that if a conflict arises it asks for
the correct implementation via context menu

Before

    class IntroduceMockFieldExample {
        void example() {
            var result = new TestClass([cursor]null,null);
        }
    }

After

    class IntroduceMockFieldExample {
        @Mock
        String string1;
        @Mock
        String string2;
    
        void example() {
            var result = new TestClass(string1, string2);
        }
    }

PS: you can also introduce @Spy fields like this, just use **CTRL + SHIFT + M, CTRL + F** instead.

----------------------

## Add Mock Method (implements [VariableFinder](#variablefinder))

Creates a mock method for the given method. Put cursor on a variable/field, execute "Add Mock Method"
action (**CTRL + M, CTRL + M**), select a method and a mock for a method will appear. It is smart enough to find the
correct argument matchers of Mockito based on the type.

Before

    var string[cursor] = Mockito.mock(String.class);

After

    var string = Mockito.mock(String.class);
    Mockito.when(string.compareTo(any())).thenReturn([cursor]null);

----------------------

## Add Verify Method (implements [VariableFinder](#variablefinder))

Similiar to the [Add Mock Method](#add-mock-method) shortcut, this shortcut (**CTRL + M, CTRL + C**)
will introduce a correctly implemented Mockito.verify of the chosen method.

Also the [Variable finder](#new-feature-variable-finder) can search the current method for variables, to create the
Mockito.Verify directly from anywhere.

Before

    var string[cursor] = Mockito.mock(String.class);

After

    var string = Mockito.mock(String.class);
    Mockito.verify(string).compareTo([cursor]any());

----------------------

## Multiline Counting

Every need to rename alot of variables in a sequential way? **CTRL + M, CTRL + 1** on a multiline cursor will open a
small popup to set a starting index and an interval. The plugin will insert the start number and then write sequential
numbers based on the interval.

PS: this does also work for characters, just insert "A" or "a" to count in uppercase and lowercase letters

![multilinecount](multilinecount.gif)

----------------------

## NamingSchemeCycling

Ever needed to switch naming schemes of a selection? **CTRL + SHIFT + ALT + U** and the selection will cycle through
naming schemes. Currently supported are: **camelCase, dot.case, PascalCase, SNAKE_CASE and doener-case**.

![namingscheme](namingscheme.gif)

----------------------

## SmartPaste

Copy some code and paste it onto a selected text via **ALT + V**. Depending on the content of the clipboard and the text
selection a different behaviour occurs (with multi cursor support!):

| Clipboard              | Selection                  | Result                                                      |
|------------------------|----------------------------|-------------------------------------------------------------|
| coolMethod(            | "abc"                      | coolMethod("abc")                                           |
| coolMethod(            | "abc";                     | coolMethod("abc");                                          |
| "def"                  | coolMethod("abc");         | coolMethod("def");                                          |
| var newString = "abc"; | public void coolMethod() { | public void coolMethod { <br/>var newString = "abc"; <br/>} |
| <any-html-brackets>    | any cool text              | <any-html-brackets>any cool text</any-html-brackets>        |

![smartpaste](smartinsertion.gif)

----------------------

## Switch Mockito Mocking Variant

Put cursor (or multi cursor!) on a line and switch between Mockito.when and Mockito.doReturn 
mock method variants via **CTRL + M, CTRL + S**. You can also just select the whole text segment to refactor
mockings, which are longer then 1 line.

![mockvariant](mockvariant.gif)

----------------------

## Lombok to MapStruct generator

**Currently pretty experimental**

NEW: Moved to a new plugin called [MapStructor](https://github.com/AskMeAgain/MapStructor) with more features.

----------------------

## Insertion CodeLense

A small code editor opens when pressing **CTRL + I, CTRL + A**.
The cursor is placed at the top of the current file, to enter new annotations.
Pressing **escape** closes the window

Press **CTRL + I, CTRL + F** to put the cursor on the next field;

----------------------

## VariableFinder

if your cursor is not on a variable and you press a shortcut which implements the variable finder, the plugin will
search for all possible variable declarations and will provide you with a list of possible cases to chose from. It will
then apply the shortcut to the selected variable:

![mock](mockmethod.gif)
