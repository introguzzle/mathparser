package ru.introguzzle.mathparser.function;

import ru.introguzzle.mathparser.common.SyntaxException;

public class IllegalFunctionInvocationException extends SyntaxException {

    public IllegalFunctionInvocationException(String message) {
        super(message);
    }
}
