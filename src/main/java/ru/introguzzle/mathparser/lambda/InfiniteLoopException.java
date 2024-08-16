package ru.introguzzle.mathparser.lambda;

public class InfiniteLoopException extends RuntimeException {
    public InfiniteLoopException() {
        super("Possible infinite loop detected");
    }

    public InfiniteLoopException(String message) {
        super(message);
    }
}
