package ru.contract;

import ru.exceptions.MathSyntaxException;
import ru.variable.Variables;

public interface Parser<T> {
    T parse(Expression expression) throws MathSyntaxException;
    T parse(Expression expression, Variables variables) throws MathSyntaxException;

    T parseOrNull(Expression expression);
    T parseOrNull(Expression expression, Variables variables);
}
