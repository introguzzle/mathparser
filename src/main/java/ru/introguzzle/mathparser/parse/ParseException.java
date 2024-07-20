package ru.introguzzle.mathparser.parse;

import ru.introguzzle.mathparser.common.SyntaxException;

public abstract class ParseException extends SyntaxException {

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
