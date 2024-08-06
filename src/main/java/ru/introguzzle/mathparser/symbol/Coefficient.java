package ru.introguzzle.mathparser.symbol;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.tokenize.token.type.SymbolType;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

public class Coefficient<T extends Number> extends MutableSymbol<T> {

    public Coefficient(String name, T value) {
        super(name, value);
    }

    @Override
    public @NotNull Type type() {
        return SymbolType.COEFFICIENT;
    }
}
