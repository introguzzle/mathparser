package ru.introguzzle.mathparser.common;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.expression.Expression;

import java.io.Serial;

/**
 * Provides human-readable error of parsing/tokenizing
 */
public abstract class SyntaxException extends Exception {
    @Serial
    private static final long serialVersionUID = 3643097586057214855L;

    private final Expression expression;

    public SyntaxException(@NotNull String message, @NotNull Expression expression, int offset) {
        super(message + "\n" + ExceptionUtilities.generatePointer(expression.getString(), offset));
        this.expression = expression;
    }

    public final Expression getExpression() {
        return expression;
    }
}
