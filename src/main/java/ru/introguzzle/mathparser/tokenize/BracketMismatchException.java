package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.expression.Expression;

public class BracketMismatchException extends BracketException {
    public BracketMismatchException(@NotNull Expression expression, int offset) {
        super("Invalid brackets", expression, offset);
    }
}
