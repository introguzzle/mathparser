package ru.introguzzle.mathparser.tokenize.token.type;

public enum UnitType implements Type {
    UNIT;

    @Override
    public Category getCategory() {
        return Category.UNIT;
    }
}
