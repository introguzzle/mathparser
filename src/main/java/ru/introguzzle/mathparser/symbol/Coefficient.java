package ru.introguzzle.mathparser.symbol;

import ru.introguzzle.mathparser.tokenize.TokenType;

public class Coefficient extends MutableSymbol {

    public Coefficient(String name, double value) {
        super(name, value);
    }

    @Override
    public TokenType type() {
        return TokenType.COEFFICIENT;
    }
}
