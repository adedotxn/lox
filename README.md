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

- **Our scanner here, like most, discards comments and whitespace since those aren’t needed by the parser. Why might you want to write a scanner that does not discard those? What would it be useful for?** 
    - On comments: We might not want to completely discard comments in some scanners if the comments provide useful information, take for instance how JSDoc helps the editor annotate functions and how typescript can pick up types from JSDoc comments that are to be applied to the function/expression. Some tools also use comments to generate documentation.
    - As for whitespace, there are langauges that are sensitive to whitespace (in the sense of indentation and whatnots), like Python, YAML, so if a language is sensitive around indentation, it could be a reason to not discard whitespace

- **Adding support for block comments**
    - https://github.com/adedotxn/lox/commit/083b92b678fbf9079d9adb7bad007bf37ad3f199
    - https://github.com/adedotxn/lox/commit/ef34d77ad969077d757fca2be3a7a6acc992a161
    -  **Is adding support for nesting more work than you expected? Why?** - Yes, at first it was but rethinking my approach it turned out not to be, if we simply use a tracker to track block depths and decrement the counters on each closed block, we can effectively handle nested block comments. This is more work only in the sense that there's an internal tracker to keep.

## Chapter 5
Compiling and running the Abstract Syntax Tree Generator:

- **compile** : `javac com/craftinginterpreters/tool/GenerateAst.java`
- **run** the tool: `java com.craftinginterpreters.tool.GenerateAst com/craftinginterpreters/lox`

**Challenges**
1. ```
    expr → expr ( "(" ( expr ( "," expr )* )? ")" | "." IDENTIFIER )+
     | IDENTIFIER
     | NUMBER
    ```
    An equivalent grammar that matches the language but does not use any of the notational sugar **(My solution)**:

    ```
        expr → expr group_1 
        expr → IDENTIFIER 
        expr → NUMBER

        group_1 → "(" group_2 ")" group_1_tail
        group_1 → "." IDENTIFIER group_1_tail

        group_1_tail → group_1
        group_1_tail → £

        group_2 → expr group_3
        group_2 → £

        group_3 → "," expr group_3
        group_3 → £
    ```
    I noticed I did have to come up with/use a way to imply an empty production because the example in the chapter did not have one.
    <img width="1523" alt="Untitled" src="https://github.com/user-attachments/assets/c22c0a3a-c2b2-4c9a-bdf5-66db3aa627f6" />
2. Defining a visitor class for our syntax tree classes that takes an expression, converts it to RPN, and returns the resulting string. [Commit Diff at filename: `com/craftinginterpreters/lox/RPNPrinter.java`](https://github.com/adedotxn/lox/commit/1574c84f540093abbc3abde1a2d9b7321b597c29#diff-2719416a6e0e348d673f68f7f0850d5295661df963a62b244127ac037ac82418)

    
## Chapter 6
**Challenges**
1. added support for comma expressions -> [here](https://github.com/adedotxn/lox/commit/5dae57913c14de194fd3059d36781e846b0b651b)
    - updated grammar 
    ```

    expression     → comma ;
    comma          → equality (, equality)* ;
    equality       → comparison ( ( "!=" | "==" ) comparison )* ;
    comparison     → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
    term           → factor ( ( "-" | "+" ) factor )* ;
    factor         → unary ( ( "/" | "*" ) unary )* ;
    unary          → ( "!" | "-" ) unary
                | primary ;
    primary        → NUMBER | STRING | "true" | "false" | "nil"
                | "(" expression ")" ;
    ```

2. Ternary operators are **right associative**. Associativity refers to how operators of the same precedence are grouped when they appear in a sequence without parenthesis. Determines the implicit parenthesization of the expression. Not to be confused with evalution order, which is a runtime behaviour whlie associativity has to do wuth parser structure. 
`condition1 ? 1 : condition2 ? 2 : condition3 ? 3 : 4` will be parsed as `condition1 ? 1 : (condition2 ? 2 : (condition3 ? 3 : 4))`, sreuctured like an if/else.

 🧠 Parsing vs Precedence – Quick Note

In expression parsing, operator precedence is enforced by the structure of the recursive descent parser, not during evaluation.

 - Higher precedence operators appear lower in the grammar, so they get matched first during parsing.

 - Lower precedence rules are higher in the grammar, but they only wrap what's already been parsed.

 - The parser builds an expression tree, where tighter-binding (higher precedence) operations are deeper.

 - Evaluation follows the tree, so precedence is already baked in.

 `3 + 4 * 5
`  is parsed as 
```     +
    / \
   3   *
      / \
     4   5

Binary(
  left: Literal(3),
  operator: '+',
  right: Binary(
    left: Literal(4),
    operator: '*',
    right: Literal(5)
  )
)
```

## Chapter 8
1. [Commit](https://github.com/adedotxn/lox/commit/38d9b6f716b694a5d8e66a8263d216dc7ec59490) -- This allows the REPL to execute statements, as well as evaluate an expression and display the result.
![Screenshot from 2025-05-24 09-18-00](https://github.com/user-attachments/assets/3f24cee7-b2ef-46cf-8655-f920176c703d)

2. [Commit](https://github.com/adedotxn/lox/commit/a03b7ebf203db0c0e7d6baffcbd7dfc7c815b1d9)
   ![Screenshot from 2025-05-24 08-43-26](https://github.com/user-attachments/assets/603a8911-d9e6-4d94-a807-854a095097bf)

3. ```
   var a = 1;
   
   {
       var a = a + 2;
       print a;
    }
   ``` 
This prints 3 in the block and 1 outside the block. "What did you expect it to do?" "Is it what you think it should do?" I expected it to behave like it did, outer scope does not affect the initialization in the inner scope (`var a` outside is different from `var a` inside the block, and rightfully so, despite having the same variable names, do not clash (environments ftw!!)) ..and yes, I think this is what it should do. 

   
