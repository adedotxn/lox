package com.craftinginterpreters.lox;

import static com.craftinginterpreters.lox.TokenType.*;
import java.util.List;

class Parser {
    private static class ParseError extends RuntimeException {}
    
    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    Expr parse() {
        try {
           return expression();
        } catch (ParseError e) {
            return null;
        }
    }

    private Expr expression() {
        return equality();
    }

    private Expr equality() {
        Expr expr = comparison();

        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        };

        return expr;
    };

    private Expr comparison() {
        Expr expr = term();

        while(match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr term() {
        Expr expr = factor();

        while(match(MINUS, PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr factor() {
        Expr expr = unary();

        while(match(SLASH, STAR)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr unary() {
        if(match(BANG, MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }

        return primary();
    }

    private Expr primary() {
        if(match(FALSE)) return new Expr.Literal(false);
        if(match(TRUE)) return new Expr.Literal(true);
        if(match(NIL)) return new Expr.Literal(null);

        if(match(NUMBER, STRING)) {
            return new Expr.Literal(previous().literal);
        }

        if(match(LEFT_PAREN)) {
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }

        throw error(peek(), "Expect expression.");
    };

    // checks if current token is of type and consumes it if it is, otherwise returns false and leaves the current token alone
    private boolean match(TokenType ...types) {
        for(TokenType type: types) {
            if(check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private Token consume(TokenType type, String message) {
        if(check(type)) return advance();

        throw error(peek(), message);
    }

    private boolean check(TokenType type) {
        if(isAtEnd()) return false;
        return peek().type == type;
    }

    // consumes the current token and returns it
    private Token advance() {
        if(!isAtEnd()) current++;
        return previous();
    }

    // checks if we have run out of tokens
    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    // returns the current token we have yet to consume
    private Token peek() {
        return tokens.get(current);
    }

    // returns the most recently consumed token
    private Token previous() {
        return tokens.get(current - 1);
    }

    // The error() method returns the error instead of throwing it because we want to let the calling method inside the parser decide whether to unwind or not.
    // Some parse errors occur in places where the parser isn’t likely to get into a weird state and we don’t need to synchronize. In those places, we simply report the error and keep on truckin’.
    private ParseError error(Token token, String message) {
        Lox.error(token, message);
        return new ParseError();
    }

    // The synchronize() method is called when we hit a parse error. It tries to find a place in the code where we can start parsing again.
    // It discards tokens until it thinks it has found a statement boundary. After catching a ParseError, we’ll call this and then we are hopefully back in sync.
    private void synchronize() {
        advance();
        
        // we syncronize on statement boundaries, and we know that a statement ends with a semicolon or mostly start with a keyword. 
        // When next token is any of those, we're prob about to start a statement
        while(!isAtEnd()) {
            if(previous().type == SEMICOLON) return;

            switch(peek().type) {
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
            }

            advance();
        }
    }

}
