package ru.introguzzle.mathparser.tokenize;

import ru.introguzzle.mathparser.expression.Expression;

public class UnknownOperatorException extends TokenizeException {
    public UnknownOperatorException(CharSequence operator, Expression expression, int offset) {
        super("Unknown operator: " + operator, expression, offset);
    }

    public UnknownOperatorException(Character current, Expression expression, int offset) {
        super("Unknown operator: " + current, expression, offset);
    }
}
