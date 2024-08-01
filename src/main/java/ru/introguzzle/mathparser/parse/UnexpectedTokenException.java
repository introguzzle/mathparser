package ru.introguzzle.mathparser.parse;

import ru.introguzzle.mathparser.tokenize.token.Token;
import ru.introguzzle.mathparser.tokenize.token.Tokens;

public class UnexpectedTokenException extends ParseException {
    public UnexpectedTokenException(Tokens tokens, Token token) {
        super(
                "Unexpected token: '" + token.getType() + "' at pos " + tokens.getPosition() + " in expression",
                tokens.toExpression(),
                token.getOffset()
        );
    }

    public UnexpectedTokenException(Tokens tokens, int position) {
        super(
                "Unexpected token: '" + tokens.get(position).getType() + "' at pos " + position + " in expression",
                tokens.toExpression(),
                tokens.get(position).getOffset()
        );
    }
}
