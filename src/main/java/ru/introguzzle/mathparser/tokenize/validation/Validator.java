package ru.introguzzle.mathparser.tokenize.validation;

import ru.introguzzle.mathparser.tokenize.token.Token;

@FunctionalInterface
public interface Validator {
    /**
     *
     * @param prev Previous token
     * @param token Current token
     * @param next Next token
     * @return False if validation fails
     */
    boolean validate(Token prev, Token token, Token next);
    default Token provide(Token prev, Token token, Token next) {
        if (!validate(prev, token, next)) {
            return prev;
        }

        return null;
    }
}
