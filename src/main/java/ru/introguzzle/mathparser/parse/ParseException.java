package ru.introguzzle.mathparser.parse;

import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.expression.Expression;

public abstract class ParseException extends SyntaxException {

    public ParseException(String message, Expression expression, int offset) {
        super(message, expression, offset);
    }
}
