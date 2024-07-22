package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.Optionable;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;
import ru.introguzzle.mathparser.tokenize.token.Tokens;

import java.util.List;

public interface Tokenizer extends Optionable<TokenizerOptions> {
    @NotNull Tokens tokenize(@NotNull Expression expression, @NotNull Context context) throws TokenizeException;

    List<ImmutableSymbol> getConstants();
    List<Function> getFunctions();
}
