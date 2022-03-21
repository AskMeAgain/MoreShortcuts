# Intellij Shortcut Additions

This plugin adds some shortcuts to intellij idea

## Installation

Get it from the [Intellij Marketplace](https://plugins.jetbrains.com/plugin/17814-shortcut-additions)

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

PS: this does also work for characters, just insert A or a to count in uppercase and lowercase letters

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

| Selection                  | Clipboard              | Result                                                      |
|----------------------------|------------------------|-------------------------------------------------------------|
| "abc"                      | coolMethod(            | coolMethod("abc")                                           |
| "abc";                     | coolMethod(            | coolMethod("abc");                                          |
| coolMethod("abc");         | "def"                  | coolMethod("def");                                          |
| public void coolMethod() { | var newString = "abc"; | public void coolMethod { <br/>var newString = "abc"; <br/>} |

![smartpaste](smartinsertion.gif)

----------------------

## Switch Mockito Mocking Variant

Put cursor (or multi cursor!) on a line and switch between Mockito.when and Mockito.doReturn mock method variants via **CTRL + M, CTRL + S**.

![mockvariant](mockvariant.gif)


## Lombok to MapStruct generator

Select any text which has (close to) the same format as the example,
Press **CTRL + G, CTRL + M** and this plugin will generate a MapStruct mapper.
It is _not perfect_ but will speed up the normal boring mapping pretty 
significantly. If it cannot match anything, then it will not fill in the blanks.
It can handle any nesting.

<details><summary>Before</summary>
<p>

    var result = ComplexOutputs.builder()
        .multiplications(Multiplications.builder()
            .number1mul8(123)
            .number1mul4(input.getNumber1())
            .number1mul2(input.getNumber1())
            .build())
        .summations(Summations.builder()
            .a("a")
            .aa(input.getNumber1().toString())
            .aaaa("abc")
            .build())
        .build();

</p>
</details>
<details><summary>After</summary>
<p>

    package ask.me.again.mapstruct.builder;
    
    import org.mapstruct.Mapper;
    import org.mapstruct.Mapping;
    
    @Mapper
    public interface ComplexOutputsMapper {

        @Mapping(target = "multiplications.number1mul8", source = "")
        @Mapping(target = "multiplications.number1mul4", source = "number1")
        @Mapping(target = "multiplications.number1mul2", source = "number1")
        @Mapping(target = "summations.a", source = "")
        @Mapping(target = "summations.aa", source = "number1")
        @Mapping(target = "summations.aaaa", source = "")
        ComplexOutputs map(Object input);
    
    }

</p>
</details>

----------------------

## VariableFinder

if your cursor is not on a variable and you press a shortcut which implements the variable finder, the plugin will
search for all possible variable declarations and will provide you with a list of possible cases to chose from. It will
then apply the shortcut to the selected variable:

![mock](mockmethod.gif)
