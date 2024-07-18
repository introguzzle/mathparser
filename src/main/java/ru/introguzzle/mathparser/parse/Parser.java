package ru.introguzzle.mathparser.parse;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.MathSyntaxException;
import ru.introguzzle.mathparser.expression.Expression;

import java.util.Optional;

public interface Parser<T extends Number> {
    T parse(@NotNull Expression expression) throws MathSyntaxException;
    T parse(@NotNull Expression expression, @NotNull Context context) throws MathSyntaxException;
    Optional<T> tryParse(@Nullable Expression expression);
    Optional<T> tryParse(@Nullable Expression expression, @Nullable Context context);
}
