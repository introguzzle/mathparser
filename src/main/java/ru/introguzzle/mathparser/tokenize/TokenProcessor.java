package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.tokenize.token.Tokens;

@FunctionalInterface
public interface TokenProcessor {
    @NotNull Tokens process(@NotNull Tokens tokens);
}
