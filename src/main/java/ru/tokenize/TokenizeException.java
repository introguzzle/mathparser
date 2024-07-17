package ru.tokenize;

import ru.common.MathSyntaxException;

public abstract class TokenizeException extends MathSyntaxException {

    public TokenizeException(String message) {
        super(message);
    }
}
