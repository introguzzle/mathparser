package ru.introguzzle.mathparser.parse;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.definition.FunctionDefinition;
import ru.introguzzle.mathparser.definition.FunctionDefinitionType;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.group.FunctionGroup;
import ru.introguzzle.mathparser.symbol.Coefficient;
import ru.introguzzle.mathparser.symbol.MutableSymbol;
import ru.introguzzle.mathparser.tokenize.FunctionDefinitionTokenizer;
import ru.introguzzle.mathparser.tokenize.Tokenizer;
import ru.introguzzle.mathparser.tokenize.token.Tokens;

import java.util.function.Supplier;

public class FunctionDefinitionParser implements Parser<Double> {

    private final FunctionDefinitionTokenizer<Double> tokenizer;
    private final Parser<Double> parser;

    public FunctionDefinitionParser() {
        this.tokenizer = new FunctionDefinitionTokenizer<>() {

            @Override
            public Supplier<MutableSymbol<Double>> getDefaultFactory(CharSequence name, Double value) {
                return () -> new Coefficient<>(name.toString(), value);
            }

            @Override
            public Double getDefaultValue() {
                return 0.0;
            }
        };

        this.parser = new MathParser(tokenizer);
    }

    @Override
    public Double parse(@NotNull Expression expression) throws SyntaxException {
        return parser.parse(expression);
    }

    @Override
    public Double parse(@NotNull Expression expression, @NotNull Context<Double> context) throws SyntaxException {
        return parser.parse(expression, context);
    }

    @Override
    public Double parse(@NotNull Tokens tokens, Context<Double> context) throws SyntaxException {
        return parser.parse(tokens, context);
    }

    @Override
    public Tokenizer getTokenizer() {
        return parser.getTokenizer();
    }

    public record ParserResult(double value,
                               FunctionDefinitionType type) {}

    public
    ParserResult parseDefinition(FunctionDefinition<Double> definition,
                                 Context<Double> context)
            throws SyntaxException {

        FunctionGroup group = tokenizer.tokenizeDefinition(definition, context);
        double value = parse(group, context);
        FunctionDefinitionType type = group.getType();

        return new ParserResult(value, type);
    }

    private double parse(FunctionGroup group, Context<Double> context) throws SyntaxException {
        return parse(group.getTokens(), context);
    }
}
