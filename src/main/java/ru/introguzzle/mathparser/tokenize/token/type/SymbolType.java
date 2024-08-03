package ru.introguzzle.mathparser.tokenize.token.type;

public enum SymbolType implements Type {
    CONSTANT, VARIABLE, COEFFICIENT, IMAGINARY_UNIT;

    @Override
    public Category getCategory() {
        return Category.SYMBOL;
    }
}
