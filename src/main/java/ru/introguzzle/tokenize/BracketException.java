package ru.introguzzle.tokenize;

public abstract class BracketException extends TokenizeException {
    public BracketException(String message) {
        super(message);
    }
}
