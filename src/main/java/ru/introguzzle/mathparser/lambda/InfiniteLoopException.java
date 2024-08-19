package ru.introguzzle.mathparser.lambda;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public class InfiniteLoopException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -581503637398126528L;

    public InfiniteLoopException() {
        super("Possible infinite loop detected");
    }

    public InfiniteLoopException(@NotNull String message) {
        super(message);
    }
}
