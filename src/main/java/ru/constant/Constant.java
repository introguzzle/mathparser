package ru.constant;

import ru.symbol.ImmutableSymbol;

public abstract class Constant extends ImmutableSymbol {

    public Constant(String name, double value) {
        super(name, value);
    }
}
