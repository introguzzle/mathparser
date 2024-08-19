package ru.introguzzle.mathparser.function.real;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public class IllegalFunctionInvocationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3643097586057214855L;

    public IllegalFunctionInvocationException(@NotNull String message) {
        super(message);
    }
}
