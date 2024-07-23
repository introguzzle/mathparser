package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.introguzzle.mathparser.common.*;
import ru.introguzzle.mathparser.definition.FunctionDefinition;
import ru.introguzzle.mathparser.definition.FunctionDefinitionType;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.symbol.MutableSymbol;
import ru.introguzzle.mathparser.tokenize.token.FunctionTokensProxy;
import ru.introguzzle.mathparser.tokenize.token.SimpleToken;
import ru.introguzzle.mathparser.tokenize.token.Token;
import ru.introguzzle.mathparser.tokenize.token.Tokens;

import java.util.Map;
import java.util.function.Supplier;

public abstract class FunctionDefinitionTokenizer extends MathTokenizer {

    private Resolver<FunctionDefinition, FunctionDefinitionType> resolver;

    public abstract Supplier<MutableSymbol> getDefaultFactory(CharSequence name, double value);
    public abstract double getDefaultValue();

    public
    FunctionDefinitionTokenizer setResolver(Resolver<FunctionDefinition, FunctionDefinitionType> resolver) {
        this.resolver = resolver;
        return this;
    }

    public FunctionDefinitionTokenizer() {

    }

    public
    FunctionDefinitionTokenizer(@Nullable Resolver<FunctionDefinition, FunctionDefinitionType> resolver) {
        this.resolver = resolver;
    }

    public
    FunctionDefinitionTokenizer(@NotNull Map<String, ? extends Function> functions,
                                @Nullable Resolver<FunctionDefinition, FunctionDefinitionType> resolver) {
        super(functions);
        this.resolver = resolver;
    }

    public synchronized
    @NotNull FunctionTokensProxy tokenize(@NotNull FunctionDefinition definition,
                                          @NotNull Context context)
            throws TokenizeException {

        Tokens tokens = super.tokenize(definition, context);
        FunctionDefinitionType type = resolve(definition, tokens);

        return new FunctionTokensProxy(tokens, type);
    }

    protected
    FunctionDefinitionType resolve(FunctionDefinition definition,
                                             Tokens tokens) {
        if (resolver != null) {
            return resolver.resolve(definition);
        }

        int split = definition.getDefinitionSpliterator();

        if (tokens.getCoefficientCount() > 0) {
            return split > 0
                    ? FunctionDefinitionType.EXPLICIT_FUNCTION_WITH_PARAMETERS
                    : FunctionDefinitionType.PARAMETRIC_FUNCTION_WITH_PARAMETERS;
        }

        return split > 0
                ? FunctionDefinitionType.EXPLICIT_FUNCTION
                : FunctionDefinitionType.PARAMETRIC_FUNCTION;
    }

    @Override
    protected
    Result findFromContext(@Mutates Context context,
                           CharSequence symbols,
                           int start) {
        MutableSymbol symbol = context.getSymbols().stream()
                .filter(Nameable.match(symbols))
                .findFirst()
                .orElseGet(() -> {
                    MutableSymbol fromSupplier = getDefaultFactory(symbols, getDefaultValue()).get();
                    context.addSymbol(fromSupplier);
                    return fromSupplier;
                });

        Token token = new SimpleToken(symbol.type(), symbols, start);
        return new Result(true, token);
    }
}
