package ru.introguzzle.mathparser.function;

import ru.introguzzle.mathparser.common.MathSyntaxException;

public class IllegalFunctionInvocationException extends MathSyntaxException {

    public IllegalFunctionInvocationException(String message) {
        super(message);
    }
}
