package ru.introguzzle.tokenize;

import ru.introguzzle.common.MathSyntaxException;

public abstract class TokenizeException extends MathSyntaxException {

    public TokenizeException(String message) {
        super(message);
    }
}
