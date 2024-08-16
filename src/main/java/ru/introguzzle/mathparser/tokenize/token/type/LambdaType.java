package ru.introguzzle.mathparser.tokenize.token.type;

public enum LambdaType implements Type {
    LAMBDA;

    @Override
    public Category getCategory() {
        return Category.LAMBDA;
    }
}
