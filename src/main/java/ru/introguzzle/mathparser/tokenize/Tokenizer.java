package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.naming.Context;
import ru.introguzzle.mathparser.common.options.Configurable;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.group.Group;

public interface Tokenizer extends Configurable<TokenizerOptions> {
    @NotNull Group tokenize(@NotNull Expression expression,
                            @NotNull Context<?> context)
            throws TokenizeException;

    @Override
    void setOptions(TokenizerOptions options);

    @Override
    TokenizerOptions getOptions();
}
