package ru.introguzzle.mathparser.tokenize.token.type;

public interface Type {
    int ordinal();
    Category getCategory();

    String name();

    default boolean isTerminal() {
        return getCategory().isTerminal();
    }

    enum Category implements Type {
        OPERATOR,
        PARENTHESIS,
        NUMBER,
        SPECIAL,
        FUNCTION,
        DECLARATION,
        SYMBOL,
        TERMINAL,
        COMMA;

        @Override
        public Category getCategory() {
            return this;
        }

        public boolean isTerminal() {
            return this == TERMINAL;
        }
    }
}
