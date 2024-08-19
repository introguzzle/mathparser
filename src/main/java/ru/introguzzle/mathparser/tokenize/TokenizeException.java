package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.expression.Expression;

import java.io.Serial;

public abstract class TokenizeException extends SyntaxException {
    @Serial
    private static final long serialVersionUID = 3643097586057214855L;

    public TokenizeException(@NotNull String message, @NotNull Expression expression, int offset) {
        super(message, expression, offset);
    }
}
