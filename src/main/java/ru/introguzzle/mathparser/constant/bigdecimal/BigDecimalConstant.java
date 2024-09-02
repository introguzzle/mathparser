package ru.introguzzle.mathparser.constant.bigdecimal;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;
import ru.introguzzle.mathparser.tokenize.token.type.SymbolType;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

import java.math.BigDecimal;

public abstract class BigDecimalConstant extends ImmutableSymbol<BigDecimal> {
    public BigDecimalConstant(String name, BigDecimal value) {
        super(name, value);
    }

    @Override
    public @NotNull Type type() {
        return SymbolType.BIG_DECIMAL_CONSTANT;
    }
}
