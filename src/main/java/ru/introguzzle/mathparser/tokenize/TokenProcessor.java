package ru.introguzzle.mathparser.tokenize;

import ru.introguzzle.mathparser.tokenize.token.Tokens;

@FunctionalInterface
public interface TokenProcessor {
    Tokens process(Tokens tokens) throws UnknownOperatorException;
}
