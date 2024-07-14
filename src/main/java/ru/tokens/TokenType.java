package ru.tokens;

public enum TokenType {
    OPERATOR_ADD, // +
    OPERATOR_SUB, // -
    OPERATOR_MUL, // *
    OPERATOR_DIV, // /
    OPERATOR_EXP, // **
    LEFT_BRACKET, // (
    RIGHT_BRACKET, // )
    NUMBER,
    COMMA, // ,
    FUNCTION_NAME,
    EOF,
    CONSTANT,
    VARIABLE,

    OPERATOR_AND, // &
    OPERATOR_OR, // |
    OPERATOR_XOR, // ^

    OPERATOR_NOT, // !
    OPERATOR_BITWISE_NOT, // ~
    OPERATOR_LEFT_SHIFT, // <<
    OPERATOR_RIGHT_SHIFT, // >>

    OPERATOR_EQUALS,  // ==
    OPERATOR_NOT_EQUALS, // !=
    OPERATOR_GREATER,  // >
    OPERATOR_LESS,  // <
    OPERATOR_GREATER_OR_EQUALS, // >=
    OPERATOR_LESS_OR_EQUALS  // <=

}
