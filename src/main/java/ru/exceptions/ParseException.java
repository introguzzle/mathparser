package ru.exceptions;

public class ParseException extends MathSyntaxException {

    public ParseException() {
        super();
    }
    public ParseException(String message) {
        super(message);
    }
}
