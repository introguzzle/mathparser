package ru.introguzzle.mathparser.tokenize;

public class BracketMismatchException extends BracketException {
    public BracketMismatchException() {
        super("Invalid brackets");
    }
}
