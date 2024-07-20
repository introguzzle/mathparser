package ru.introguzzle.mathparser.generate;

import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.expression.MathExpression;
import ru.introguzzle.mathparser.tokenize.Token;
import ru.introguzzle.mathparser.tokenize.TokenType;
import ru.introguzzle.mathparser.tokenize.Tokenizer;
import ru.introguzzle.mathparser.tokenize.Tokens;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CachedExpressionGenerator implements Generator<Expression> {
    private final Tokenizer tokenizer;
    private GeneratorOptions options = new GeneratorOptions(GeneratorOptions.INCLUDE_FLOATS) {};


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
        Context context = pair.getValue();

        try {
            Tokens tokens = this.tokenizer.tokenize(expression, context);
            Tokens changed = swapNumbers(tokens);

            return new MathExpression(changed.getTokens()
                    .stream()
                    .map(Token::getData)
                    .collect(Collectors.joining()));



        } catch (SyntaxException e) {
            // e.g. failed to swap numbers
            return expression;
        }
    }

    private Tokens swapNumbers(Tokens tokens) {
        List<Token> result = new ArrayList<>();

        for (Token token: tokens) {
            if (token.getTokenType() == TokenType.NUMBER) {
                float f = Random.randomFloat(options.min, options.max);
                result.add(new Token(TokenType.NUMBER, Float.toString(f)));
                continue;
            }

            result.add(token);
        }

        return new Tokens(result);
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
