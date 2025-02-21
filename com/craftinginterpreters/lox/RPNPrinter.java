package com.craftinginterpreters.lox;


class RPNPrinter implements Expr.Visitor<String> {
    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        StringBuilder builder = new StringBuilder();

        builder.append(expr.left.accept(this));
        builder.append(" ");

        builder.append(expr.right.accept(this));
        builder.append(" ");

        builder.append(expr.operator.lexeme);

        return builder.toString();
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        // In RPN, grouping is implicit in the structure
        return expr.expression.accept(this);
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        // Something like -5 becomes 5 negate;
        String operator = expr.operator.lexeme;
        if (operator.equals("-")) operator = "negate";

        return expr.right.accept(this) + " " + operator;
    }



    public static void main(String[] args) {
        Expr expression = new Expr.Binary(
            new Expr.Unary(
                new Token(TokenType.MINUS, "-", null, 1),
                new Expr.Literal(123)
            ),

            new Token(TokenType.STAR, "*", null, 1),

            new Expr.Grouping(
                new Expr.Literal(45.67))
            );
    
        System.out.println(new RPNPrinter().print(expression));

        // (1 + 2) * (4 - 3)
        Expr expression2 =  new Expr.Binary(
            new Expr.Grouping(
                new Expr.Binary(
                    new Expr.Literal(1),
                    new Token(TokenType.STAR, "+", null, 1),
                    new Expr.Literal(2)
                )
            ),

            new Token(TokenType.STAR, "*", null, 1),

             new Expr.Grouping(
                new Expr.Binary(
                    new Expr.Literal(4),
                    new Token(TokenType.STAR, "-", null, 1),
                    new Expr.Literal(3)
                )
            )
        );

        System.out.println(new RPNPrinter().print(expression2));
    }
}