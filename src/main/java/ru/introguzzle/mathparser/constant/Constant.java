package ru.introguzzle.mathparser.constant;

import ru.introguzzle.mathparser.symbol.ImmutableSymbol;
import ru.introguzzle.mathparser.tokenize.TokenType;
import ru.introguzzle.mathparser.tokenize.Type;

public abstract class Constant extends ImmutableSymbol {

    public Constant(String name, double value) {
        super(name, value);
    }

    @Override
    public Type type() {
        return TokenType.CONSTANT;
    }
}
