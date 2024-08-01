package ru.introguzzle.mathparser.tokenize.token.type;

public enum TerminalType implements Type {
    TERMINAL;

    @Override
    public Category getCategory() {
        return Category.TERMINAL;
    }
}
