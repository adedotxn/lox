package com.craftinginterpreters.com;

import static com.craftinginterpreters.lox.TokenType.*;

import java.util.List;

class Parser {

    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }
}
