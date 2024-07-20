package ru.introguzzle.mathparser.tokenize;

import ru.introguzzle.mathparser.common.ExceptionUtilities;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.expression.Expression;

public abstract class TokenizeException extends SyntaxException {
    private final Expression expression;

    public TokenizeException(String message, Expression expression, int offset) {
        super(message + "\n" + ExceptionUtilities.generatePointer(expression.getString(), offset));
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }
}
