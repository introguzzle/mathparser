package ru.introguzzle.mathparser.tokenize;

public enum TokenType implements Type {
    LEFT_PARENTHESIS(Category.PARENTHESIS),
    RIGHT_PARENTHESIS(Category.PARENTHESIS),
    NUMBER(Category.NUMBER),
    FUNCTION_NAME(Category.FUNCTION),
    EOF(Category.SPECIAL),

    COMMA(Category.SPECIAL),

    DECLARATION(Category.DECLARATION),
    DECLARATION_END(Category.DECLARATION),

    CONSTANT(Category.SYMBOL),
    VARIABLE(Category.SYMBOL),
    COEFFICIENT(Category.SYMBOL),

    OPERATOR_ADD(Category.OPERATOR), // +
    OPERATOR_SUB(Category.OPERATOR), // -
    OPERATOR_MUL(Category.OPERATOR), // *
    OPERATOR_DIV(Category.OPERATOR), // /
    OPERATOR_EXP(Category.OPERATOR), // **

    OPERATOR_AND(Category.OPERATOR), // &
    OPERATOR_OR(Category.OPERATOR), // |
    OPERATOR_XOR(Category.OPERATOR), // ^
    OPERATOR_NOT(Category.OPERATOR), // !
    OPERATOR_BITWISE_NOT(Category.OPERATOR), // ~
    OPERATOR_LEFT_SHIFT(Category.OPERATOR), // <<
    OPERATOR_RIGHT_SHIFT(Category.OPERATOR), // >>

    OPERATOR_EQUALS(Category.OPERATOR),  // ==
    OPERATOR_NOT_EQUALS(Category.OPERATOR), // !=
    OPERATOR_GREATER(Category.OPERATOR),  // >
    OPERATOR_LESS(Category.OPERATOR),  // <
    OPERATOR_GREATER_OR_EQUALS(Category.OPERATOR), // >=
    OPERATOR_LESS_OR_EQUALS(Category.OPERATOR);   // <=

    private final Category category;

    TokenType(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return this.category;
    }
}
