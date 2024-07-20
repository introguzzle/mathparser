package ru.introguzzle.mathparser.tokenize;

import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.Optionable;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;

import java.util.List;

public interface Tokenizer extends Optionable<TokenizerOptions> {
    Tokens tokenize(Expression expression, Context context) throws TokenizeException;

    List<ImmutableSymbol> getConstants();
    List<Function> getFunctions();
}
