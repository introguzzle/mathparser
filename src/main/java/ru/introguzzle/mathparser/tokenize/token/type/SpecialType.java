package ru.introguzzle.mathparser.tokenize.token.type;

public enum SpecialType implements Type {
    COMMA;

    @Override
    public Category getCategory() {
        return Category.COMMA;
    }
}
