package ru.introguzzle.mathparser.parse;

import ru.introguzzle.mathparser.common.MathSyntaxException;

public abstract class ParseException extends MathSyntaxException {

    public ParseException() {
        super();
    }
    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
