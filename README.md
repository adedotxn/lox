# crafting interpreters

## To run as REPL

- **compile** : `javac com/craftinginterpreters/lox/*.java`
- **run** the repl: `java com.craftinginterpreters.lox.Lox`
- type in a valid/invalid expression

## Chapter 2 Challenges

- Open source language of choice: **TypeScript**
    - Scanner Implementation: [typescript/src/compiler/scanner.ts](https://github.com/microsoft/TypeScript/blob/main/src/compiler/scanner.ts)
    - Parser Implementation: [typescript/src/compiler/parser.ts](https://github.com/microsoft/TypeScript/blob/main/src/compiler/parser.ts)
    - The parser and scanner for TS are handwritten.

- Some reasons to not JIT 
    - I think one reason to not use JIT is that since JITs need to know the architecture of the host machine, there's an additional overhead in determining that and that could hamper compiler speed.
    - Another reason is that because JITs compile to host architecture machine code and is not specifically targeted to an architecture, there may be some platform specific optimisation that'll increase performance that a JIT compiler will not be able to do, limiting it's performance and maybe portability


## Chapter 4 Challenges

- **The lexical grammars of Python and Haskell are not regular. What does that mean, and why aren’t they?**
    - The lexical grammars of Python and Haskell aren't regular because they depend on whitespace in a way that most languages don't. Regular grammars can be described with regular expressions, but Python uses indentation to define blocks, making its grammar context-sensitive. Haskell also uses indentation and has layout rules that make its lexical structure context-dependent. This means their grammars can't be fully captured by regular expressions, which is why they aren't regular. \
        References: [Python Language Reference -  _Lexical analysis_](https://docs.python.org/3/reference/lexical_analysis.html), [The Haskell 98 Report - _Lexical Structure_](https://www.haskell.org/onlinereport/lexemes.html), [https://www.reddit.com/r/compsci/comments/kkzn3r/the_lexical_grammars_of_python_and_haskell_are/](https://www.reddit.com/r/compsci/comments/kkzn3r/the_lexical_grammars_of_python_and_haskell_are/)

- **How does spaces affect code is parsed in CoffeeScript, Ruby, and the C preprocessor?**
    - [ChatGTPT generrated answer, becasue I do not write any of the languages and can only look through the docs long enough] : In CoffeeScript, whitespace, specifically indentation, is crucial because it defines block structures, similar to Python. For example, the indentation level after an if or else keyword determines the scope of each block. You can learn more about this in the CoffeeScript Documentation. In Ruby, while spaces and indentation don't affect parsing directly since the language uses keywords and end statements to define blocks, they do enhance readability and can impact method calls, especially when parentheses are omitted. For instance, a method call without parentheses like puts "Hello, world" relies on the correct use of spaces for clarity. More details can be found in the Ruby Documentation. The C preprocessor generally ignores spaces, but they can be significant in macro definitions and conditional directives, ensuring that macros behave as intended. For example, correct spacing in a macro definition like #define MAX 100 is essential. For further information, refer to the C Preprocessor Documentation.
    - References: [https://www.reddit.com/r/Compilers/comments/1b6u8yx/question_how_does_spaces_affect_code_is_parsed_in/](https://www.reddit.com/r/Compilers/comments/1b6u8yx/question_how_does_spaces_affect_code_is_parsed_in/), [https://stackoverflow.com/questions/9014970/why-does-coffeescript-require-whitespace-after-map](https://stackoverflow.com/questions/9014970/why-does-coffeescript-require-whitespace-after-map), [https://stackoverflow.com/questions/37796947/spaces-inserted-by-the-c-preprocessor](https://stackoverflow.com/questions/37796947/spaces-inserted-by-the-c-preprocessor)

- **Adding support for block comments**
    - WIP