package ru.introguzzle.mathparser.tokenize.token.type;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Nameable;

public enum SpecialType implements Type, Nameable {
    COMMA;

    @Override
    public Category getCategory() {
        return Category.COMMA;
    }

    @Override
    public @NotNull String getName() {
        return ",";
    }

    @Override
    public @NotNull Type type() {
        return this;
    }
}
