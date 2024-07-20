package ru.introguzzle.mathparser.common;

import ru.introguzzle.mathparser.tokenize.TokenType;
import java.util.function.Predicate;

public interface Nameable {
    String getName();

    default String describe() {
        return getClass().getSimpleName();
    }

    default boolean nameEquals(CharSequence sequence) {
        return match(sequence).test(this);
    }

    static Predicate<Nameable> match(CharSequence sequence) {
        return s -> s.getName().contentEquals(sequence);
    }

    static Predicate<String> match(Nameable nameable) {
        return s -> s.contentEquals(nameable.getName());
    }

    TokenType type();

    boolean equals(Object o);
}
