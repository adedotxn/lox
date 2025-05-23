package com.craftinginterpreters.lox;

import java.util.HashMap;
import java.util.Map;


//  where the bindings that associate variables to values are stored == environments
class Environment {
    private final Map<String, Object> values = new HashMap<>();  
    final Environment enclosing;

    // this constructor is used for the global scope's environment which ends the chain
    Environment() {
        enclosing = null;
    }

    // This other constructor creates a new local scope nested inside the given outer one.
    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }
    
    
    Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }

        // if the variable is not found in the current environment, we check the enclosing one
        if(enclosing != null) {
            return enclosing.get(name);
        }

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    // diff. between assignment and definition is that assignment is not allowed to create a new variable. 
    void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
          values.put(name.lexeme, value);
          return;
        }

        // Again, if the variable isn’t in this environment, it checks the outer one, recursively.
        if(enclosing != null) {
            enclosing.assign(name, value);
            return;
        }

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    void define(String name, Object value) {
        values.put(name, value);
    }
}
