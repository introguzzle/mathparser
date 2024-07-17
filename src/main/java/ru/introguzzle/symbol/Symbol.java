package ru.introguzzle.symbol;

import java.util.function.Predicate;

public interface Symbol<T extends Number> {
    String getName();
    T getValue();

    default boolean nameEquals(CharSequence sequence) {
        return match(sequence).test(this);
    }

    static Predicate<Symbol<?>> match(CharSequence sequence) {
        return s -> s.getName().contentEquals(sequence);
    }
}
