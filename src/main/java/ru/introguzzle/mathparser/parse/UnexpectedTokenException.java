package ru.introguzzle.mathparser.parse;

import ru.introguzzle.mathparser.tokenize.Token;
import ru.introguzzle.mathparser.tokenize.Tokens;

public class UnexpectedTokenException extends ParseException {
    public UnexpectedTokenException(Tokens tokens, Token token) {
        super("Unexpected token: '" + token.getTokenType() + "' at pos " + tokens.getPosition() + " in expression");
    }
}
