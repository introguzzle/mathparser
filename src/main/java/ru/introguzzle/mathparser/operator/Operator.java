package ru.introguzzle.mathparser.operator;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Nameable;
import ru.introguzzle.mathparser.tokenize.token.type.OperatorType;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

import java.util.List;
import java.util.function.Function;

public interface Operator<T extends Number> extends Nameable,
        Associative,
        Priorable,
        Function<List<T>, T> {
    int VARIADIC = -1;
    int UNARY = 1;
    int BINARY = 2;

    int getRequiredOperands();
    default boolean isVariadic() {
        return getRequiredOperands() == VARIADIC;
    }

    @Override
    default @NotNull Type type() {
        return OperatorType.OPERATOR;
    }
}
