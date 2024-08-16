package ru.introguzzle.mathparser.tokenize.validation;

import ru.introguzzle.mathparser.tokenize.token.Token;

@FunctionalInterface
public interface Validator {
    boolean validate(Token prev, Token token, Token next) throws ValidationException;
}
