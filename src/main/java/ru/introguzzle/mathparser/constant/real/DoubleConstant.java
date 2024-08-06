package ru.introguzzle.mathparser.constant.real;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;
import ru.introguzzle.mathparser.tokenize.token.type.SymbolType;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

public abstract class DoubleConstant extends ImmutableSymbol<Double> {

    public DoubleConstant(String name, double value) {
        super(name, value);
    }

    @Override
    public @NotNull Type type() {
        return SymbolType.CONSTANT;
    }
}
