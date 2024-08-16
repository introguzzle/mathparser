package ru.introguzzle.mathparser.generate;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.math.Number;
import ru.introguzzle.mathparser.tokenize.token.NumberToken;
import ru.introguzzle.mathparser.tokenize.token.SimpleToken;
import ru.introguzzle.mathparser.tokenize.token.Token;
import ru.introguzzle.mathparser.tokenize.token.type.NumberType;
import ru.introguzzle.mathparser.tokenize.token.type.Type;

public class NumberSwapper extends Swapper {
    public NumberSwapper(GeneratorOptions options) {
        super(options);
    }

    @Override
    public @NotNull Type getType() {
        return NumberType.NUMBER;
    }

    @Override
    public @NotNull Token apply(Token token, int offset) {
        if (token instanceof NumberToken numberToken) {
            String newFloat = Random.getRandomFloat(getOptions().getMin(), getOptions().getMax(), numberToken.getNumber().getRadix());
            Number number = new Number(newFloat, numberToken.getNumber().getRadix());

            return new NumberToken(numberToken.getType(), number, token.getOffset() + offset);
        }

        return new SimpleToken(token.getType(), token.getData(), token.getOffset() + offset);
    }
}
