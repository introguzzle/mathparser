package ru.introguzzle.mathparser.tokenize.token.type;

public enum DeclarationType implements Type {
    DECLARATION, DECLARATION_TERMINAL;

    @Override
    public Category getCategory() {
        return Category.DECLARATION;
    }
}
