package ru.introguzzle.mathparser.common.resolve;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.expression.Expression;

@FunctionalInterface
public interface Resolver<T extends Expression, R> {
    @NotNull R resolve(@NotNull T expression);
}
