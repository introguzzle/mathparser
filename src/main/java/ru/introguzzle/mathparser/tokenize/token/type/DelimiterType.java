package ru.introguzzle.mathparser.tokenize.token.type;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Nameable;

public enum DelimiterType implements Type, Nameable {
    ARROW("->"),
    COMMA(","),
    SEMICOLON(";");

    private final String name;

    DelimiterType(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull Type type() {
        return this;
    }

    @Override
    public Category getCategory() {
        return Category.COMMA;
    }
}
