package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.expression.Expression;

public class UnknownCharacterException extends TokenizeException {

    public UnknownCharacterException(char current, Expression expression, int offset) {
        super(createMessage(current, offset), expression, offset);
    }

    @NotNull
    private static String createMessage(char current, int offset) {
        return "Unexpected character: '" + current + "' at pos " + offset + " in expression";
    }
}
