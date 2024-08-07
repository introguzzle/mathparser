package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.Optionable;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.group.Group;

public interface Tokenizer extends Optionable<TokenizerOptions> {
    @NotNull Group tokenize(@NotNull Expression expression, @NotNull Context<?> context) throws TokenizeException;
}
