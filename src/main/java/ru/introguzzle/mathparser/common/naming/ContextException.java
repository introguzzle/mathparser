package ru.introguzzle.mathparser.common.naming;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public abstract class ContextException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4265170816591987802L;

    public ContextException() {
        super();
    }

    public ContextException(@NotNull String message) {
        super(message);
    }

    public ContextException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }
}
