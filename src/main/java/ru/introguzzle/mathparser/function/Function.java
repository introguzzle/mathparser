package ru.introguzzle.mathparser.function;

import ru.introguzzle.mathparser.common.Nameable;
import ru.introguzzle.mathparser.tokenize.token.TokenType;
import ru.introguzzle.mathparser.tokenize.token.Type;

import java.util.List;

public interface Function extends java.util.function.Function<List<Double>, Double>, Nameable {
    int getRequiredArguments();

    boolean isVariadic();

    default IllegalFunctionInvocationException createException(int given) {
        return new IllegalFunctionInvocationException(FunctionUtilities.createExceptionMessage(given, this));
    }

    @Override
    default Type type() {
        return TokenType.FUNCTION_NAME;
    }
}
