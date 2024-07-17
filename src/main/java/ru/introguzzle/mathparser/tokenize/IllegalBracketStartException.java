package ru.introguzzle.mathparser.tokenize;

public class IllegalBracketStartException extends BracketException {
    public IllegalBracketStartException() {
        super("Illegal brackets");
    }
}
