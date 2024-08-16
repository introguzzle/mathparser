package ru.introguzzle.mathparser.common.naming;

public abstract class ContextException extends RuntimeException {
    public ContextException() {
        super();
    }

    public ContextException(String message) {
        super(message);
    }

    public ContextException(String message, Throwable cause) {
        super(message, cause);
    }
}
