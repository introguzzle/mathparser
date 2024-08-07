package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.introguzzle.mathparser.common.*;
import ru.introguzzle.mathparser.definition.FunctionDefinition;
import ru.introguzzle.mathparser.definition.FunctionDefinitionType;
import ru.introguzzle.mathparser.expression.ExpressionIterator;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.group.FunctionGroup;
import ru.introguzzle.mathparser.symbol.MutableSymbol;
import ru.introguzzle.mathparser.symbol.Variable;
import ru.introguzzle.mathparser.tokenize.token.*;
import ru.introguzzle.mathparser.tokenize.token.type.DeclarationType;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class FunctionDefinitionTokenizer<T extends Number>
        extends MathTokenizer {
    private Resolver<FunctionDefinition<T>, FunctionDefinitionType> resolver;

    public abstract Supplier<MutableSymbol<T>> getDefaultFactory(CharSequence name, T value);
    public abstract T getDefaultValue();

    public
    FunctionDefinitionTokenizer<T> setResolver(Resolver<FunctionDefinition<T>, FunctionDefinitionType> resolver) {
        this.resolver = resolver;
        return this;
    }

    public FunctionDefinitionTokenizer() {

    }

    public
    FunctionDefinitionTokenizer(@Nullable Resolver<FunctionDefinition<T>, FunctionDefinitionType> resolver) {
        this.resolver = resolver;
    }

    public
    FunctionDefinitionTokenizer(@NotNull Map<String, ? extends Function<?>> functions,
                                @Nullable Resolver<FunctionDefinition<T>, FunctionDefinitionType> resolver) {
        super(functions);
        this.resolver = resolver;
    }

    public synchronized
    @NotNull FunctionGroup tokenizeDefinition(@NotNull FunctionDefinition<T> definition,
                                              @NotNull Context<Double> context)
            throws TokenizeException {
        ExpressionIterator iterator = definition.iterator();
        Buffer buffer = new Buffer(iterator, definition, context);

        Tokens declarationTokens = new SimpleTokens();
        declarationTokens.add(handleDefinition(buffer, definition));

        Tokens mainTokens = super.start(buffer);

        if (mainTokens.get(0).getType() == DeclarationType.DECLARATION_TERMINAL) {
            declarationTokens.add(mainTokens.getTokenList().removeFirst());
        }

        return new FunctionGroup(declarationTokens, mainTokens, resolve(definition, mainTokens));
    }

    protected Token handleDefinition(Buffer buffer,
                                     FunctionDefinition<T> definition) {
        int split = definition.getDefinitionSpliterator();
        Variable<T> variable = definition.getVariable();

        if (!buffer.context().contains(variable.getName())) {
            Context<T> parent = setContextParent(buffer.context());
            parent.addSymbol(variable);
        }

        buffer.iterator().setCursor(split);

        return new SimpleToken(
                DeclarationType.DECLARATION,
                definition.getString().substring(0, split),
                0
        );
    }

    protected
    FunctionDefinitionType resolve(FunctionDefinition<T> definition,
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
    protected SearchResult findFromContext(@Mutates Context<?> context,
                                           CharSequence symbols,
                                           int start) {
        Context<T> parent = setContextParent(context);
        Optional<? extends MutableSymbol<?>> optional = context.getSymbol(symbols.toString());
        MutableSymbol<T> symbol;

        if (optional.isPresent()) {
            @SuppressWarnings("unchecked")
            MutableSymbol<T> mutableSymbol = (MutableSymbol<T>) optional.get();
            symbol = mutableSymbol;
        } else {
            symbol = getDefaultFactory(symbols, getDefaultValue()).get();
            parent.addSymbol(symbol);
        }

        Token token = new SimpleToken(symbol.type(), symbols, start);
        return new SearchResult(true, token);
    }

    private <U extends Number> Context<T> setContextParent(Context<U> context) {
        Context<T> parent = new NamingContext<>();

        if (context.getParent() == null) {
            try {
                @SuppressWarnings("unchecked")
                Context<U> c = (Context<U>) parent;
                context.setParent(c);
            } catch (ClassCastException e) {
                throw new NumberTypeException("This tokenizer doesn't support other number types than doubles", e);
            }
        }

        try {
            @SuppressWarnings("unchecked")
            Context<T> c = (Context<T>) context.getParent();
            return c;
        } catch (ClassCastException e) {
            throw new NumberTypeException("This tokenizer doesn't support other number types than doubles", e);
        }
    }

    @Override
    protected SearchResult find(CharSequence symbols, int start) {
        SearchResult result = super.find(symbols, start);

        return "=".contentEquals(symbols)
                ? SearchResult.reduce(result, new SearchResult(
                    true,
                    new SimpleToken(DeclarationType.DECLARATION_TERMINAL, symbols, start)))
                : result;
    }
}
