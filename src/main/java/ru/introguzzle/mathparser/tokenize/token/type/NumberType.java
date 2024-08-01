package ru.introguzzle.mathparser.tokenize.token.type;

public enum NumberType implements Type {
    NUMBER;

    @Override
    public Category getCategory() {
        return Category.NUMBER;
    }
}
