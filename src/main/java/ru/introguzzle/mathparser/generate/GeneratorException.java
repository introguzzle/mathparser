package ru.introguzzle.mathparser.generate;

public class GeneratorException extends RuntimeException {
    public GeneratorException() {

    }

    public GeneratorException(String message) {
        super(message);
    }

    public GeneratorException(String message, Throwable cause) {
        super(message, cause);
    }
}
