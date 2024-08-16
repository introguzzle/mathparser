package ru.introguzzle.mathparser.symbol;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.tokenize.token.type.SymbolType;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

public class LambdaArgument<T extends Number> extends MutableSymbol<T> {
    public LambdaArgument(String name, T value) {
        super(name, value);
    }

    @Override
    public @NotNull Type type() {
        return SymbolType.LAMBDA_ARGUMENT;
    }
}
