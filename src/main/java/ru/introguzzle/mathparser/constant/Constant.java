package ru.introguzzle.mathparser.constant;

import ru.introguzzle.mathparser.symbol.ImmutableSymbol;
import ru.introguzzle.mathparser.tokenize.TokenType;

public abstract class Constant extends ImmutableSymbol {

    public Constant(String name, double value) {
        super(name, value);
    }

    @Override
    public TokenType type() {
        return TokenType.CONSTANT;
    }
}
