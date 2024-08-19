package ru.introguzzle.mathparser.generate;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.common.naming.Context;
import ru.introguzzle.mathparser.common.naming.NamingContext;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.symbol.MutableSymbol;
import ru.introguzzle.mathparser.symbol.Variable;
import ru.introguzzle.mathparser.tokenize.FunctionDefinitionTokenizer;
import ru.introguzzle.mathparser.tokenize.TokenProcessor;
import ru.introguzzle.mathparser.tokenize.Tokenizer;
import ru.introguzzle.mathparser.tokenize.token.Tokens;

import java.util.function.Supplier;

public class CachedExpressionGenerator implements Generator<Expression> {
    private GeneratorOptions options;

    private final Tokenizer tokenizer;
    private final TokenProcessor[] tokenProcessors;

    public CachedExpressionGenerator() {
        this(new FunctionDefinitionTokenizer<>() {
            @Override
            public Supplier<MutableSymbol<Number>> getDefaultFactory(CharSequence name, Number value) {
                return () -> new Variable<>(name.toString(), value);
            }

            @Override
            public Number getDefaultValue() {
                return 0.0;
            }
        }, new CachedGeneratorOptions());
    }

    public CachedExpressionGenerator(Tokenizer tokenizer) {
        this(tokenizer, new CachedGeneratorOptions());
    }

    public CachedExpressionGenerator(Tokenizer tokenizer, GeneratorOptions options) {
        this(tokenizer, options,
                new NumberSwapper(options),
                new FunctionSwapper(options, tokenizer)
        );
    }

    public CachedExpressionGenerator(@NotNull Tokenizer tokenizer,
                                     @NotNull GeneratorOptions options,
                                     @NotNull TokenProcessor... tokenProcessors) {
        this.tokenizer = tokenizer;
        this.options = options;
        this.tokenProcessors = tokenProcessors;
    }

    @Override
    public Expression generate() {
        ExpressionStorage.Pair pair = Random.fromCollection(ExpressionStorage.EXPRESSIONS);
        if (pair == null) {
            throw new GeneratorException("No expressions found");
        }

        Expression expression = pair.getKey();
        Context<Double> context;

        if (tokenizer instanceof FunctionDefinitionTokenizer<?>) {
            context = new NamingContext<>();
        } else {
            context = pair.getValue();
        }

        try {
            Tokens tokens = tokenizer.tokenize(expression, context).getTokens();

            for (TokenProcessor tokenProcessor : tokenProcessors) {
                tokens = tokenProcessor.apply(tokens);
            }

            return tokens.toExpression();
        } catch (SyntaxException e) {
            return expression;
        }    }

    public static void main(String[] args) {
        CachedExpressionGenerator g = new CachedExpressionGenerator();

        System.out.println(g.generate());
    }

    @Override
    public GeneratorOptions getOptions() {
        return options;
    }

    @Override
    public void setOptions(GeneratorOptions options) {
        this.options = options;
    }
}
