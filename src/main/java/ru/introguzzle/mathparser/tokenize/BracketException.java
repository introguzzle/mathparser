package ru.introguzzle.mathparser.tokenize;

public abstract class BracketException extends TokenizeException {
    public BracketException(String message) {
        super(message);
    }
}
