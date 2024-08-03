package ru.introguzzle.mathparser.tokenize;

import ru.introguzzle.mathparser.expression.Expression;

public class UnknownOperatorException extends TokenizeException {
    private final CharSequence operator;

    public UnknownOperatorException(CharSequence operator, Expression expression, int offset) {
        super("Unknown operator: " + operator, expression, offset);
        this.operator = operator;
    }

    public UnknownOperatorException(Character operator, Expression expression, int offset) {
        super("Unknown operator: " + operator, expression, offset);
        this.operator = Character.toString(operator);
    }

    public CharSequence getOperator() {
        return operator;
    }
}
