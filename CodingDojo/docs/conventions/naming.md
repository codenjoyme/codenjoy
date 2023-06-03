Naming convention in the project can be divided into several components, and
each section of this document will be devoted to them:
* methods
* classes
* test code
* fields
* variables
* constants

First of all, it should be said that the basis was taken
[jcc 97th year](https://www.oracle.com/technetwork/java/codeconventions-150003.pdf).
Differences from it will be described here and argued.

The main principle in naming is conciseness.
The name should contain at least 1 keyword that reports
the main purpose of the structure. The remaining words in the name should
help the main word. If there is an opportunity to take information from the context:
local variables, method, field, class, package - it is better not to overload
this information with the selected name. For example:

* `String stringPetNameVariable;`  
  + the word `string` from the name can be excluded, as there is a type with the same name nearby
  + the word `variable` is excluded - from the code it can be seen that this is a variable
  + the word `pet` is excluded - from the name of the class it can be seen that this is Pet
  + remains `String name`

If there is 1 word left, great. 2 is tolerable for methods, constants and classes, but
you need to think if it comes to local variables, fields or method parameters.
3 words - most often a bust and you can try to "twist" the name again.
4 words - exactly bust.

## <a id="method"></a> Methods

The most important thing in the method name is 1 word - a verb that reports the main action
the method is committed. The method often does one thing, otherwise
it should be divided into two or more.

### <a id="get-set"></a> Get-Set methods

For get/set methods, if possible, the words get/set are excluded, because
from the context it is clear what the method does. get - without parameters, but with return type.
set - with 1 parameter, but void. The name - exactly corresponds to the name of the field.

Lombok is often used to generate these empty methods, especially if pojo
it is planned to be given to a framework or library that expects inside methods
getSomething/setSomething. If pojo does not leave the project - get/set
omitted to increase readability.

## <a id="class"></a> Class

The main word in the class name is a noun that will
determine the essence of the object. Additional words help to understand more. Word
from the name should be excluded if this word can be found somewhere else in
class environment: package, class fields, field types or if this removal is not
leads to a deterioration in readability.

## <a id="test"></a> Tests

In the names of test methods, the underscore character is allowed.

The notation `shouldSomething_whenSometin` is used, sometimes explanations are required
in the form of additions `shouldSomething_whenSometin_caseInfo`. When choosing a name
for a new method, you should look around and study the names of other methods
so that the new test method is co-cultural in this test class.

## <a id="variable"></a> Variables

Local variables should be made as short as possible. Provided that the method
will be small (1-3-5-7 lines) extra overload of words of local variables
will complicate readability. 1 word is the golden mean.

The variable returned from the method is always called result, the rest is clear
from the context: method name, type of returned result.

All abbreviations (except `Point pt`) are turned into a normal name, for example:
`i` -> `index`, `с` -> `count`. Abbreviations are also not used in lambdas.

Why `Point` - an exception? Readability. The word point is often used in methods
often and if you use `point` instead of the abbreviation `pt` then readability deteriorates.

```
private void removeBlasts() {
    blasts().clear();

    for (Point pt : destroyedObjects) {
        if (pt instanceof TreasureBox) {
            boxes().removeAt(pt);
            dropPerk(pt, dice);
        } else if (pt instanceof GhostHunter) {
            hunters().removeAt(pt);
        } else if (pt instanceof Ghost) {
            ghosts().removeAt(pt);
        }
    }

    cleanDestroyedObjects();
}
```

If you follow the general conventions, it will be worse. In a small method on 9 lines,
7 words point is clearly overkill. There are many such methods in games, where in the code there will be
more words point than others.


```
private void removeBlasts() {
    blasts().clear();

    for (Point point : destroyedObjects) {
        if (point instanceof TreasureBox) {
            boxes().removeAt(point);
            dropPerk(point, dice);
        } else if (point instanceof GhostHunter) {
            hunters().removeAt(point);
        } else if (point instanceof Ghost) {
            ghosts().removeAt(point);
        }
    }

    cleanDestroyedObjects();
}
```

## <a id="field"></a> Fields

Good field name consists of 1 word, worse if 2. In the name you should not use
words that are contained in the context: class name, package, field type.

## <a id="constant"></a> Constants

Here also works the rule of conciseness - do not use words that already exist
in the environment of the constant: type of constant, class name, package.