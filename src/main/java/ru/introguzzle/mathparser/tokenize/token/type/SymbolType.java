package ru.introguzzle.mathparser.tokenize.token.type;

public enum SymbolType implements Type {
    CONSTANT, VARIABLE, COEFFICIENT, IMAGINARY_UNIT, COMPLEX_CONSTANT;

    @Override
    public Category getCategory() {
        return Category.SYMBOL;
    }
}
