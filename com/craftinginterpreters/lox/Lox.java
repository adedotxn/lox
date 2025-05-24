package com.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {


  /** We make the field static so that successive calls to run() inside a REPL session reuse the same interpreter. Necessary for global variables in a REPL session */
  private static final Interpreter interpreter = new Interpreter();

  static boolean hadError = false;
  static boolean hadRuntimeError = false;
  private static boolean isReplMode = false;

  public static void main(String[] args) throws IOException {
    if (args.length > 1) {
      System.out.println("Usage: jlox [script]");
      System.exit(64); 
    } else if (args.length == 1) {
      runFile(args[0]);
    } else {
      runPrompt();
    }
  };

  private static void runFile(String path) throws IOException {
    byte[] bytes = Files.readAllBytes(Paths.get(path));
    run(new String(bytes, Charset.defaultCharset()));

    if (hadError) System.exit(65);
    if (hadRuntimeError) System.exit(70);

    
  };

  private static void runPrompt() throws IOException {
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);

    isReplMode = true; 


    for(;;) {
        System.out.print("> ");
        String line = reader.readLine();
        if(line == null) break;
        run(line);
        hadError = false;
    };
  };

  private static void run(String source) {
    Scanner scanner = new Scanner(source);
    List<Token> tokens = scanner.scanTokens();

    Parser parser = new Parser(tokens);
    // Expr expression = parser.parse();
    List<Stmt> statements = parser.parse();

    if(hadError) return;

    // Uncomment this to see the AST
    // System.out.println(new AstPrinter().print(expression));
    


    if (isReplMode && statements.size() == 1 && statements.get(0) instanceof Stmt.Expression) {
      Stmt.Expression exprStmt = (Stmt.Expression) statements.get(0);
      Object result = interpreter.evaluateExpression(exprStmt.expression); 
      System.out.println(stringify(result));

      // this is basically like the former way interpreter.interpret used to work for expressions where it used evaluate instead of execute
    } else {
      interpreter.interpret(statements);
    }
  }


  static void error(int line, String message) {
    report(line, "", message);
  };

  private static void report(int line, String where, String message) {
    System.err.println(
        "[line " + line + "] Error" + where + ": " + message
    ); 
    hadError = true;
  }

  static void error(Token token, String message) {
    if(token.type == TokenType.EOF) {
        report(token.line, " at end", message);
    } else {
        report(token.line, " at '" + token.lexeme + "'", message);
    }
  }

  static void runtimeError(RuntimeError error) {
    System.err.println(error.getMessage() + "\n[line " + error.token.line + "]");
    hadRuntimeError = true;
  }

  private static String stringify(Object object) {
    if (object == null) return "nil";

    if (object instanceof Double) {
      String text = object.toString();
      if (text.endsWith(".0")) {
        text = text.substring(0, text.length() - 2);
      }
      return text;
    }

    return object.toString();
  }

}