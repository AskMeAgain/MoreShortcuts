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

## Introduce Mock To Variable

Introduces Mockito mocks for every "null" value of the class. Place cursor inside the brackets and run 
the "Introduce Mock To Variable" action (**CTRL + M, CTRL + V**). In case of a conflict a context menu pops up to resolve it.

![](gifs/introduce-mock-variable.gif)

----------------------

## Introduce Mock To Field

Introduces Mockito mock fields for every "null" value of the class. Place cursor inside the brackets
and run the "Introduce Mock To Field" action (**CTRL + M, CTRL + F**). Its also smart enough,
that if a conflict arises it asks for the correct implementation via context menu

![](gifs/introduce-mock-field.gif)

----------------------

## Add Mock Method

Creates a mock method for the given method. Put cursor on a variable/field, execute "Add Mock Method" 
action (**CTRL + M, CTRL + M**), select a method and a mock for a method will appear. 
It is smart enough to find the correct argument matchers of Mockito based on the type.

![](gifs/add-mock-method.gif)

