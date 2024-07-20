package ru.introguzzle.mathparser.common;

public abstract class SyntaxException extends Exception {

    public SyntaxException() {
        super();
    }
    public SyntaxException(String message) {
        super(message);
    }

    public SyntaxException(String message, Throwable cause) {
        super(message, cause);
    }
}
