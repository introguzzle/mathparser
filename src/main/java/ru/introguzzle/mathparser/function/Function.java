package ru.introguzzle.mathparser.function;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Nameable;
import ru.introguzzle.mathparser.function.real.FunctionUtilities;
import ru.introguzzle.mathparser.function.real.IllegalFunctionInvocationException;
import ru.introguzzle.mathparser.operator.Operator;
import ru.introguzzle.mathparser.operator.Priorities;
import ru.introguzzle.mathparser.tokenize.token.type.FunctionType;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

import java.util.List;

public interface Function<T extends Number> extends
        java.util.function.Function<List<T>, T>,
        Nameable {
    int getRequiredArguments();

    boolean isVariadic();

    @NotNull private IllegalFunctionInvocationException getInvocationException(int given) {
        return new IllegalFunctionInvocationException(FunctionUtilities.createExceptionMessage(given, this));
    }

    @NotNull T evaluate(List<T> arguments);

    default @NotNull T apply(List<T> arguments) {
        int given = arguments.size();

        if (isVariadic()) {
            if (getRequiredArguments() > given) {
                throw getInvocationException(given);
            } else {
                return evaluate(arguments);
            }
        }

        if (getRequiredArguments() != given) {
            throw getInvocationException(given);
        }

        return evaluate(arguments);
    }

    @Override
    default @NotNull Type type() {
        return FunctionType.FUNCTION;
    }

    default @NotNull Operator<T> toOperator() {
        return new Operator<>() {
            @Override
            public int getRequiredOperands() {
                return Function.this.getRequiredArguments();
            }

            @Override
            public T apply(List<T> operands) {
                return Function.this.apply(operands);
            }

            @Override
            public @NotNull String getName() {
                return Function.this.getName();
            }

            @Override
            public Association getAssociation() {
                return Association.LEFT;
            }

            @Override
            public int getPriority() {
                return Priorities.FUNCTION_PRIORITY;
            }
        };
    }
}
