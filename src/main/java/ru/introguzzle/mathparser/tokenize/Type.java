package ru.introguzzle.mathparser.tokenize;

public interface Type {
    int ordinal();
    Category getCategory();

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
