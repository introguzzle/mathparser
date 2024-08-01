package ru.introguzzle.mathparser.function;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Nameable;
import ru.introguzzle.mathparser.operator.Operator;
import ru.introguzzle.mathparser.tokenize.token.type.FunctionType;
import ru.introguzzle.mathparser.operator.Priorities;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

import java.util.List;

public interface Function extends java.util.function.Function<List<Double>, Double>, Nameable {
    int getRequiredArguments();

    boolean isVariadic();

    @NotNull default IllegalFunctionInvocationException createException(int given) {
        return new IllegalFunctionInvocationException(FunctionUtilities.createExceptionMessage(given, this));
    }

    @Override
    default @NotNull Type type() {
        return FunctionType.FUNCTION;
    }

    default Operator<Double> toOperator() {
        return new Operator<>() {
            @Override
            public int getPriority() {
                return Priorities.FUNCTION_PRIORITY;
            }

            @Override
            public int getRequiredOperands() {
                return getRequiredArguments();
            }

            @Override
            public Double apply(List<Double> arguments) {
                return Function.this.apply(arguments);
            }

            @Override
            public Association getAssociation() {
                return Association.LEFT;
            }
        };
    }
}
