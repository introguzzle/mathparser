package ru.function;

import ru.common.MathSyntaxException;

public class IllegalFunctionInvocationException extends MathSyntaxException {

    public IllegalFunctionInvocationException(String message) {
        super(message);
    }
}
