package ru.introguzzle.mathparser.common;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface Nameable extends Cloneable {
    @NotNull String getName();

    @NotNull default String describe() {
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

    static Map<String, Nameable> toMap(Collection<? extends Nameable> nameables) {
        return nameables.stream().collect(Collectors.toMap(Nameable::getName, n -> n));
    }

    @NotNull Type type();

    boolean equals(Object o);
}
