package ru.common;

public abstract class MathSyntaxException extends Exception {

    public MathSyntaxException() {
        super();
    }
    public MathSyntaxException(String message) {
        super(message);
    }

    public MathSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }
}
