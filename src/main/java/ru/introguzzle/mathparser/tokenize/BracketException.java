package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.expression.Expression;

public abstract class BracketException extends TokenizeException {
    public BracketException(@NotNull String message,
                            @NotNull Expression expression, int position) {
        super(message, expression, position);
    }
}
