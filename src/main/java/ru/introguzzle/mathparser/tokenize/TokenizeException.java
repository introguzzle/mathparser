package ru.introguzzle.mathparser.tokenize;

import ru.introguzzle.mathparser.common.MathSyntaxException;

public abstract class TokenizeException extends MathSyntaxException {

    public TokenizeException(String message) {
        super(message);
    }
}
