package ru.introguzzle.mathparser.constant;

import ru.introguzzle.mathparser.symbol.ImmutableSymbol;

public abstract class Constant extends ImmutableSymbol {

    public Constant(String name, double value) {
        super(name, value);
    }
}
