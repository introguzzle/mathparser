package ru.introguzzle.mathparser.tokenize.token.type;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Nameable;

public enum ParenthesisType implements Type, Nameable {
    LEFT("("),
    RIGHT(")");

    private final String name;

    ParenthesisType(String name) {
        this.name = name;
    }

    @Override
    public Category getCategory() {
        return Category.PARENTHESIS;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull Type type() {
        return this;
    }
}
