package ru.exceptions;

public class VariableMismatchException extends RuntimeException {
    public VariableMismatchException(int given, int actual) {
        super(String.format("Given %d arguments, but expression actually has %d arguments", given, actual));
    }
}
