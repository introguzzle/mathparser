package ru.introguzzle.mathparser.tokenize;

import ru.introguzzle.mathparser.expression.Expression;

public class UnknownCharacterException extends TokenizeException {

    public UnknownCharacterException(Expression expression, Character current) {
        super("Unexpected character: '" + current + "' at pos " + expression.getCursor() + " in expression");
    }
}
