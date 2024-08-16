package ru.introguzzle.mathparser.tokenize;

import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.tokenize.token.Tokens;

public class InvalidNumberFormatException extends TokenizeException {
    private final CharSequence number;

    public InvalidNumberFormatException(CharSequence number, Expression expression, int offset) {
        super("Invalid number format: " + number, expression, offset);
        this.number = number;
    }

    public InvalidNumberFormatException(CharSequence number, Tokens tokens, int offset) {
        super("Invalid number format: " + number, tokens.toExpression(), offset);
        this.number = number;
    }

    public final CharSequence getNumber() {
        return number;
    }
}
