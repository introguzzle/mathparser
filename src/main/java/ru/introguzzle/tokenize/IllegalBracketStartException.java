package ru.introguzzle.tokenize;

public class IllegalBracketStartException extends BracketException {
    public IllegalBracketStartException() {
        super("Illegal brackets");
    }
}
