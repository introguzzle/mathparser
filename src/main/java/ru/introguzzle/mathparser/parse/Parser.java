package ru.introguzzle.mathparser.parse;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.tokenize.token.Tokens;

import java.util.Optional;

public interface Parser<T extends Number> {
    T parse(@NotNull Expression expression) throws SyntaxException;
    T parse(@NotNull Expression expression, @NotNull Context context) throws SyntaxException;
    T parse(@NotNull Tokens tokens, Context context) throws SyntaxException;

    default Optional<T> tryParse(@Nullable Expression expression) {
        if (expression == null) return Optional.empty();

        try {
            return Optional.ofNullable(parse(expression));
        } catch (SyntaxException e) {
            return Optional.empty();
        }
    }

    default Optional<T> tryParse(@Nullable Expression expression, @Nullable Context context) {
        if (expression == null || context == null) return Optional.empty();

        try {
            return Optional.ofNullable(parse(expression, context));
        } catch (SyntaxException e) {
            return Optional.empty();
        }
    }
}
