package ru.tokenize;

public class BracketMismatchException extends BracketException {
    public BracketMismatchException() {
        super("Invalid brackets");
    }
}
