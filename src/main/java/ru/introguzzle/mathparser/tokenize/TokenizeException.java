package ru.introguzzle.mathparser.tokenize;

import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.expression.Expression;

public abstract class TokenizeException extends SyntaxException {
    public TokenizeException(String message, Expression expression, int offset) {
        super(message, expression, offset);
    }
}
