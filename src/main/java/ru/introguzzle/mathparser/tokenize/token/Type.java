package ru.introguzzle.mathparser.tokenize.token;

public interface Type {
    int ordinal();
    Category getCategory();

    String name();

    enum Category implements Type {
        OPERATOR,
        PARENTHESIS,
        NUMBER,
        SPECIAL,
        FUNCTION,
        DECLARATION,
        SYMBOL;

        @Override
        public Category getCategory() {
            return this;
        }
    }
}
