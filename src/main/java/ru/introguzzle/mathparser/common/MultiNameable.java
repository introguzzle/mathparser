package ru.introguzzle.mathparser.common;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface MultiNameable extends Nameable {
    @NotNull
    Set<String> getNames();

    @Override
    default boolean nameEquals(CharSequence sequence) {
        for (String name : getNames()) {
            if (name.contentEquals(sequence)) {
                return true;
            }
        }

        return Nameable.super.nameEquals(sequence);
    }
}
