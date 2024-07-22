package ru.introguzzle.mathparser.parse;

import ru.introguzzle.mathparser.tokenize.token.Token;
import ru.introguzzle.mathparser.tokenize.token.Tokens;

public class UnexpectedTokenException extends ParseException {
    public UnexpectedTokenException(Tokens tokens, Token token) {
        super("Unexpected token: '" + token.getType() + "' at pos " + tokens.getPosition() + " in expression");
    }
}
