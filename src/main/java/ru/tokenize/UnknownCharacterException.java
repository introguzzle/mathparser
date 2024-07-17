package ru.tokenize;

import ru.expression.Expression;

public class UnknownCharacterException extends TokenizeException {

    public UnknownCharacterException(Expression expression, Character current) {
        super("Unexpected character: '" + current + "' at pos " + expression.getCursor() + " in expression");
    }
}
