package ru.introguzzle.mathparser.constant.complex;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.complex.Complex;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;
import ru.introguzzle.mathparser.tokenize.token.type.SymbolType;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

public abstract class ComplexConstant extends ImmutableSymbol<Complex> {
    public ComplexConstant(String name, Complex value) {
        super(name, value);
    }

    @Override
    public @NotNull Type type() {
        return SymbolType.COMPLEX_CONSTANT;
    }
}
