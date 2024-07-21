package ru.introguzzle.mathparser.common;

import ru.introguzzle.mathparser.expression.Expression;

public interface Resolver<T extends Expression, R> {
    R resolve(T expression);
}
