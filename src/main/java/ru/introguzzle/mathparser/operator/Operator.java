package ru.introguzzle.mathparser.operator;

import ru.introguzzle.mathparser.tokenize.token.type.Priorable;

import java.util.List;
import java.util.function.Function;

public interface Operator<T extends Number> extends Priorable, Function<List<T>, T> {
    int VARIADIC = -1;

    int operands();
    default boolean isVariadic() {
        return operands() == VARIADIC;
    }
}
