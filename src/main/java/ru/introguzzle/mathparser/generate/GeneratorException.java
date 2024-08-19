package ru.introguzzle.mathparser.generate;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public class GeneratorException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3643097586057214855L;

    public GeneratorException() {

    }

    public GeneratorException(@NotNull String message) {
        super(message);
    }

    public GeneratorException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }
}
