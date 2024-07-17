package ru.introguzzle.parse;

import ru.introguzzle.tokenize.Token;
import ru.introguzzle.tokenize.Tokens;

public class UnexpectedTokenException extends ParseException {
    public UnexpectedTokenException(Tokens tokens, Token token) {
        super("Unexpected token: '" + token.getTokenType() + "' at pos " + tokens.getPosition() + " in expression");
    }
}
