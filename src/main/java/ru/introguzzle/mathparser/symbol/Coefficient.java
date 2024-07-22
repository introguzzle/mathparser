package ru.introguzzle.mathparser.symbol;

import ru.introguzzle.mathparser.tokenize.token.TokenType;
import ru.introguzzle.mathparser.tokenize.token.Type;

public class Coefficient extends MutableSymbol {

    public Coefficient(String name, double value) {
        super(name, value);
    }

    @Override
    public Type type() {
        return TokenType.COEFFICIENT;
    }
}
