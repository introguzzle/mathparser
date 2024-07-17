package ru.introguzzle.function;

import ru.introguzzle.common.MathSyntaxException;

public class IllegalFunctionInvocationException extends MathSyntaxException {

    public IllegalFunctionInvocationException(String message) {
        super(message);
    }
}
