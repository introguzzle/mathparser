package ru.parse;

import ru.common.MathSyntaxException;

public class ParseException extends MathSyntaxException {

    public ParseException() {
        super();
    }
    public ParseException(String message) {
        super(message);
    }
}
