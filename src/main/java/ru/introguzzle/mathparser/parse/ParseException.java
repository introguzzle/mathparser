package ru.introguzzle.mathparser.parse;

import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.expression.Expression;

import java.io.Serial;

public abstract class ParseException extends SyntaxException {
    @Serial
    private static final long serialVersionUID = 3643097586057214855L;

    public ParseException(String message, Expression expression, int offset) {
        super(message, expression, offset);
    }
}
