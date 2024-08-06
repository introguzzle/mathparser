package ru.introguzzle.mathparser.symbol;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.tokenize.token.type.SymbolType;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

public class Variable<N extends Number> extends MutableSymbol<N> {
    public Variable(String name, N value) {
        super(name, value);
    }

    @Override
    public @NotNull Type type() {
        return SymbolType.VARIABLE;
    }
}
