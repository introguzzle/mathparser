package ru.introguzzle.mathparser.common;

import org.jetbrains.annotations.NotNull;

public final class ExceptionUtilities {
    public static String generatePointer(@NotNull String string, int offset) {
        int repeat = Math.max(0, offset);
        String pointer = " ".repeat(repeat) + "^";

        return string + "\n" + pointer;
    }
}
