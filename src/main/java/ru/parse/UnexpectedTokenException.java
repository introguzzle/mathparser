package ru.parse;

import ru.tokenize.Token;
import ru.tokenize.Tokens;

public class UnexpectedTokenException extends ParseException {
    public UnexpectedTokenException(Tokens tokens, Token token) {
        super("Unexpected token: '" + token.getTokenType() + "' at pos " + tokens.getPosition() + " in expression");
    }
}
