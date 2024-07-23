package ru.introguzzle.mathparser.function;

public class IllegalFunctionInvocationException extends RuntimeException {

    public IllegalFunctionInvocationException(String message) {
        super(message);
    }
}
