package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.tokenize.token.Tokens;

import java.util.function.Function;

@FunctionalInterface
public interface TokenProcessor extends Function<Tokens, Tokens> {
    @NotNull Tokens apply(@NotNull Tokens tokens);

    @NotNull
    @Override
    default <V> Function<V, Tokens> compose(@NotNull Function<? super V, ? extends Tokens> before) {
        return Function.super.compose(before);
    }

    @NotNull
    @Override
    default <V> Function<Tokens, V> andThen(@NotNull Function<? super Tokens, ? extends V> after) {
        return Function.super.andThen(after);
    }

    static @NotNull TokenProcessor identity() {
        return t -> t;
    }
}
