package ru.introguzzle.function;

import java.util.List;

public interface Function extends java.util.function.Function<List<Double>, Double> {
    String getName();
    int getRequiredArguments();

    boolean isVariadic();

    default IllegalFunctionInvocationException createException(int given) {
        return new IllegalFunctionInvocationException(FunctionUtilities.createExceptionMessage(given, this));
    }
}
