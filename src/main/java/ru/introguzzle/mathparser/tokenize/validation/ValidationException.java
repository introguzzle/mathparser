package ru.introguzzle.mathparser.tokenize.validation;

import java.io.Serial;

public class ValidationException extends Exception {
    @Serial
    private static final long serialVersionUID = -4292468617517246481L;

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
