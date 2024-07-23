package ru.introguzzle.mathparser.common;

import ru.introguzzle.mathparser.expression.Expression;

public abstract class SyntaxException extends Exception {

    private final Expression expression;

    public SyntaxException(String message, Expression expression, int offset) {
        super(message + "\n" + ExceptionUtilities.generatePointer(expression.getString(), offset));
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }
}
