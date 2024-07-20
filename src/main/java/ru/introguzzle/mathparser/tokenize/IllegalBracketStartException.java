package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.expression.Expression;

public class IllegalBracketStartException extends BracketException {
    public IllegalBracketStartException(@NotNull Expression expression, int offset) {
        super("Illegal brackets", expression, offset);
    }
}
