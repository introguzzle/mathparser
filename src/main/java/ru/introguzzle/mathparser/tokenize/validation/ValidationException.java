package ru.introguzzle.mathparser.tokenize.validation;

public class ValidationException extends Exception {
    private final String data;
    private final int offset;

    public ValidationException(String message, String data, int offset) {
        super(message);
        this.data = data;
        this.offset = offset;
    }

    public String getData() {
        return data;
    }

    public int getOffset() {
        return offset;
    }
}
