package ru.introguzzle.mathparser.generate;

import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.expression.MathExpression;
import ru.introguzzle.mathparser.tokenize.*;
import ru.introguzzle.mathparser.tokenize.token.*;
import ru.introguzzle.mathparser.tokenize.token.type.NumberType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CachedExpressionGenerator implements Generator<Expression> {
    private final Tokenizer tokenizer;
    private GeneratorOptions options = new GeneratorOptions(GeneratorOptions.INCLUDE_FLOATS) {};

    {
        options.maxFloating = 1;
    }

    public CachedExpressionGenerator(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public CachedExpressionGenerator(Tokenizer tokenizer, GeneratorOptions options) {
        this.tokenizer = tokenizer;
        this.options = options;
    }

    @Override
    public Expression generate() {
        var pair = Random.pickFromCollection(ExpressionStorage.EXPRESSIONS);
        Expression expression = pair.getKey();
        Context<Double> context = pair.getValue();

        try {
            Tokens tokens = this.tokenizer.tokenize(expression, context).getTokens();
            Tokens changed = swapNumbers(tokens);

            return new MathExpression(
                    changed.getTokenList()
                    .stream()
                    .map(t -> t.getData() + " ")
                    .collect(Collectors.joining())
            );


        } catch (SyntaxException e) {
            // e.g. failed to swap numbers
            return expression;
        }
    }

    private Tokens swapNumbers(Tokens tokens) {
        List<Token> result = new ArrayList<>();

        for (Token token: tokens) {
            if (token.getType() == NumberType.NUMBER) {
                float f = Random.randomFloat(options.min, options.max);
                result.add(new SimpleToken(NumberType.NUMBER, Float.toString(f), token.getOffset()));
                continue;
            }

            result.add(token);
        }

        return new SimpleTokens(result);
    }

    @Override
    public GeneratorOptions getOptions() {
        return this.options;
    }

    @Override
    public void setOptions(GeneratorOptions options) {
        this.options = options;
    }
}
