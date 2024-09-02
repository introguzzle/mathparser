package ru.introguzzle.mathparser.common.math.algebra;

import java.io.Serial;

public class UnsupportedAlgebraOperationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 7511119392766620914L;

    public UnsupportedAlgebraOperationException() {
        super();
    }

    public UnsupportedAlgebraOperationException(String message) {
        super(message);
    }

    public UnsupportedAlgebraOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
