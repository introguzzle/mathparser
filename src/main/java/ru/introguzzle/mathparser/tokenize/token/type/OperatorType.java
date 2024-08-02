package ru.introguzzle.mathparser.tokenize.token.type;

public enum OperatorType implements Type {
    OPERATOR;

    @Override
    public Category getCategory() {
        return Category.OPERATOR;
    }
}
