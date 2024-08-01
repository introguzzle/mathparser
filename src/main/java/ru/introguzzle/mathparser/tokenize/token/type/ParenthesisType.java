package ru.introguzzle.mathparser.tokenize.token.type;

import org.jetbrains.annotations.NotNull;

public enum ParenthesisType implements ScalarType, Priorable {
    LEFT("("),
    RIGHT(")");

    private final String representation;

    ParenthesisType(String representation) {
        this.representation = representation;
    }

    @Override
    public @NotNull String getRepresentation() {
        return representation;
    }

    @Override
    public Category getCategory() {
        return Category.PARENTHESIS;
    }

    @Override
    public int getPriority() {
        return Priorities.PARENTHESIS_PRIORITY;
    }

    @Override
    public Association getAssociation() {
        return Association.NONE;
    }
}
