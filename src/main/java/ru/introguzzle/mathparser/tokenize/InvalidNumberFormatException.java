package ru.introguzzle.mathparser.tokenize;

import ru.introguzzle.mathparser.expression.Expression;

public class InvalidNumberFormatException extends TokenizeException {
    private final CharSequence number;

    public InvalidNumberFormatException(CharSequence number, Expression expression, int offset) {
        super("Invalid number format: " + number, expression, offset);
        this.number = number;
    }

    public CharSequence getNumber() {
        return number;
    }
}
