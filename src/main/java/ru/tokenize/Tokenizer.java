package ru.tokenize;

import ru.common.Context;
import ru.common.Optionable;
import ru.expression.Expression;
import ru.function.Function;
import ru.symbol.ImmutableSymbol;

import java.util.List;
import java.util.Map;

public interface Tokenizer extends Optionable<TokenizerOptions> {
    Tokens tokenize(Expression expression, Context context) throws TokenizeException;

    List<ImmutableSymbol> getConstants();
    Map<String, Function> getFunctions();
}
