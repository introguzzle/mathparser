package ru.introguzzle.mathparser.tokenize.token.type;

public enum FunctionType implements Type {
    FUNCTION;

    @Override
    public Category getCategory() {
        return Category.FUNCTION;
    }
}
