package ru.introguzzle.mathparser.tokenize.token.type;

public enum SymbolType implements Type {
    CONSTANT,
    VARIABLE,
    COEFFICIENT,
    COMPLEX_CONSTANT,
    LAMBDA_ARGUMENT;

    @Override
    public Category getCategory() {
        return Category.SYMBOL;
    }
}
