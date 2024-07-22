package ru.introguzzle.mathparser.parse;

import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.definition.FunctionDefinition;
import ru.introguzzle.mathparser.definition.FunctionDefinitionType;
import ru.introguzzle.mathparser.symbol.Coefficient;
import ru.introguzzle.mathparser.symbol.MutableSymbol;
import ru.introguzzle.mathparser.tokenize.FunctionDefinitionTokenizer;
import ru.introguzzle.mathparser.tokenize.TokenizeException;
import ru.introguzzle.mathparser.tokenize.token.FunctionTokens;

import java.util.function.Supplier;

public class FunctionDefinitionParser extends MathParser {

    public FunctionDefinitionParser() {
        super(new FunctionDefinitionTokenizer() {
            @Override
            public double getDefaultValue() {
                return 0;
            }

            @Override
            public Supplier<MutableSymbol> defaultSupplier(CharSequence name, double value) {
                return () -> new Coefficient(name.toString(), value);
            }
        });
    }

    protected
    FunctionTokens tokenize(FunctionDefinition definition,
                            Context context)
            throws TokenizeException {

        FunctionDefinitionTokenizer tokenizer = (FunctionDefinitionTokenizer) this.tokenizer;
        return tokenizer.tokenize(definition, context);
    }

    public record ParserResult(double value,
                               FunctionDefinitionType type) {}

    public
    ParserResult parse(FunctionDefinition definition,
                       Context context)
            throws SyntaxException {

        var result = tokenize(definition, context);
        double value = super.parse(result, context);
        FunctionDefinitionType type = result.getType();

        return new ParserResult(value, type);
    }
}
