package com.craftinginterpreters.lox;

import java.util.HashMap;
import java.util.Map;


//  where the bindings that associate variables to values are stored == environments
class Environment {
    private final Map<String, Object> values = new HashMap<>();  
    final Environment enclosing;


    /**
     * SENTINEL OBJECT PATTERN:
     * - We need a way to represent "uninitialized" that's different from null
     * - We can't use null because:
     * null might be a valid value (var a = nil;)
     * We need to distinguish between "never assigned" vs "assigned nil"
     * 
     * new Object() { ... } creates an anonymous subclass of Object
     * If we just did: new Object(), we'd get a generic Object
     * By creating anonymous subclass, we can override methods & we override toString()
     */
    private static final Object UNINITIALIZED = new Object() {
        @Override
        public String toString() {
            return "UNITIALIZED";
        }
    };

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
            Object value = values.get(name.lexeme);


            if (value == UNINITIALIZED) {
                throw new RuntimeError(
                    name, 
                    "Variable '" + name.lexeme + "' used before initialization."
                );
            }


            return value;
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

    void defineUninitialized(String name) {
        values.put(name, UNINITIALIZED);
    }
}
