package ru.introguzzle.mathparser.common;

public class NumberTypeException extends RuntimeException {
    public NumberTypeException() {

    }

    public NumberTypeException(String message) {
        super(message);
    }

    public NumberTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
