package ru.introguzzle.mathparser.common;

import org.jetbrains.annotations.NotNull;

public final class ExceptionUtilities {
    private ExceptionUtilities() {

    }

    public static @NotNull String generatePointer(@NotNull String string, int offset) {
        int repeat = Math.max(0, offset);
        String pointer = " ".repeat(repeat) + "^";

        return string + "\n" + pointer;
    }

    public static @NotNull String pluralize(String string, int count) {
        if (count == 1) {
            return string;
        }

        return string + "s";
    }
}
