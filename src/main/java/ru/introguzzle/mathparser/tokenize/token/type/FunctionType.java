package ru.introguzzle.mathparser.tokenize.token.type;

public enum FunctionType implements Priorable, Type {
    FUNCTION;

    @Override
    public int getPriority() {
        return Priorities.FUNCTION_PRIORITY;
    }

    @Override
    public Association getAssociation() {
        return Association.LEFT;
    }

    @Override
    public Category getCategory() {
        return Category.FUNCTION;
    }
}
