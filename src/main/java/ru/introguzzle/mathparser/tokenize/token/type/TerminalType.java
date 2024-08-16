package ru.introguzzle.mathparser.tokenize.token.type;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Nameable;

public enum TerminalType implements Type, Nameable {
    TERMINAL;

    @Override
    public Category getCategory() {
        return Category.TERMINAL;
    }

    @Override
    public @NotNull String getName() {
        return "";
    }

    @Override
    public @NotNull Type type() {
        return this;
    }
}
