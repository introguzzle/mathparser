package ru.introguzzle.mathparser.common;

import java.util.Arrays;

public interface Options {

    int getFlags();

    default boolean match(int flag) {
        return (getFlags() & flag) == flag;
    }

    default boolean matchAll(int... flags) {
        return Arrays.stream(flags).allMatch(this::match);
    }

    default boolean matchAny(int... flags) {
        return Arrays.stream(flags).anyMatch(this::match);
    }
}
