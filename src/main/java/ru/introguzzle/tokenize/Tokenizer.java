package ru.introguzzle.tokenize;

import ru.introguzzle.common.Context;
import ru.introguzzle.common.Optionable;
import ru.introguzzle.expression.Expression;
import ru.introguzzle.function.Function;
import ru.introguzzle.symbol.ImmutableSymbol;

import java.util.List;
import java.util.Map;

public interface Tokenizer extends Optionable<TokenizerOptions> {
    Tokens tokenize(Expression expression, Context context) throws TokenizeException;

    List<ImmutableSymbol> getConstants();
    Map<String, Function> getFunctions();
}
