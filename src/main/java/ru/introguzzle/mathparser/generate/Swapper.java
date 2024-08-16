package ru.introguzzle.mathparser.generate;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.options.Optionable;
import ru.introguzzle.mathparser.tokenize.TokenProcessor;
import ru.introguzzle.mathparser.tokenize.token.SimpleToken;
import ru.introguzzle.mathparser.tokenize.token.SimpleTokens;
import ru.introguzzle.mathparser.tokenize.token.Token;
import ru.introguzzle.mathparser.tokenize.token.Tokens;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

public abstract class Swapper implements
        TokenProcessor,
        Optionable<GeneratorOptions> {
    private GeneratorOptions options;

    public Swapper(GeneratorOptions options) {
        this.options = options;
    }

    @Override
    public GeneratorOptions getOptions() {
        return options;
    }

    @Override
    public void setOptions(GeneratorOptions options) {
        this.options = options;
    }

    public abstract @NotNull Type getType();
    public abstract @NotNull Token apply(Token token, int offset);

    @Override
    public @NotNull Tokens process(@NotNull Tokens tokens) {
        Tokens result = new SimpleTokens();

        int totalDifference = 0;

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            if (token.getType() == getType()) {
                Token swapped = apply(token, totalDifference);
                int difference = swapped.getData().length() - token.getLength();
                result.add(swapped);
                totalDifference += difference;
            } else {
                result.add(new SimpleToken(token.getType(), token.getData(), token.getOffset() + totalDifference, token.getLength()));
            }
        }

        return result;
    }
}
