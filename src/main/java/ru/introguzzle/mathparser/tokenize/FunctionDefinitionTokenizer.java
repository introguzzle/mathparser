package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.introguzzle.mathparser.common.*;
import ru.introguzzle.mathparser.definition.FunctionDefinition;
import ru.introguzzle.mathparser.definition.FunctionDefinitionType;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.symbol.MutableSymbol;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class FunctionDefinitionTokenizer extends MathTokenizer {

    private final Map<String, Nameable> temporary = new HashMap<>();
    private Resolver<FunctionDefinition, FunctionDefinitionType> resolver;

    public abstract Supplier<Nameable> factory(CharSequence name);

    public Map<String, Nameable> getTemporary() {
        return temporary;
    }

    public void setResolver(Resolver<FunctionDefinition, FunctionDefinitionType> resolver) {
        this.resolver = resolver;
    }

    public FunctionDefinitionTokenizer() {

    }

    public FunctionDefinitionTokenizer(@Nullable Resolver<FunctionDefinition, FunctionDefinitionType> resolver) {
        this.resolver = resolver;
    }

    public FunctionDefinitionTokenizer(@NotNull Map<String, ? extends Function> functions,
                                       @Nullable Resolver<FunctionDefinition, FunctionDefinitionType> resolver) {
        super(functions);
        this.resolver = resolver;
    }

    /**
     * Adapter class
     */
    public static class TokenizerResult extends Tokens {
        private final Map<String, Nameable> temporary = new HashMap<>();
        private final FunctionDefinitionType type;

        public Map<String, Nameable> getTemporary() {
            return temporary;
        }

        public TokenizerResult(Tokens tokens, FunctionDefinitionType type) {
            this.type = type;
            getTokens().clear();
            getTokens().addAll(tokens.getTokens());
        }

        public FunctionDefinitionType getType() {
            return type;
        }
    }

    public synchronized @NotNull
    TokenizerResult tokenize(@NotNull FunctionDefinition definition)
            throws TokenizeException {

        Tokens tokens = super.tokenize(definition, new NamingContext());
        FunctionDefinitionType type = resolve(definition, tokens);

        TokenizerResult result = new TokenizerResult(tokens, type);
        result.getTemporary().putAll(temporary);
        temporary.clear();

        return result;
    }

    protected FunctionDefinitionType resolve(FunctionDefinition definition, Tokens tokens) {
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

    private static class NotMutableSymbolException extends ContextException {
        public NotMutableSymbolException(String message) {
            super(message);
        }
    }

    @Override
    protected Result findFromContext(Context context, CharSequence symbols) {
        Nameable nameable = context.getSymbols().stream()
                .filter(Nameable.match(symbols))
                .findFirst()
                .orElseGet(() -> {
                    Nameable fromSupplier = factory(symbols).get();

                    if (fromSupplier instanceof MutableSymbol mutable) {
                        temporary.put(symbols.toString(), fromSupplier);
                        return mutable;
                    }

                    throw new NotMutableSymbolException("Not mutable symbol in context");
                });

        return new Result(true, new Token(nameable.type(), symbols));
    }
}
