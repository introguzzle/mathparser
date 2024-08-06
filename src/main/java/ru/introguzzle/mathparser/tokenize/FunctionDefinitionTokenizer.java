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
import ru.introguzzle.mathparser.tokenize.token.*;
import ru.introguzzle.mathparser.tokenize.token.type.DeclarationType;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class FunctionDefinitionTokenizer
        extends MathTokenizer {
    private Resolver<FunctionDefinition, FunctionDefinitionType> resolver;

    public abstract Supplier<MutableSymbol<?>> getDefaultFactory(CharSequence name, Number value);
    public abstract Number getDefaultValue();

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
    FunctionDefinitionTokenizer(@NotNull Map<String, ? extends Function<?>> functions,
                                @Nullable Resolver<FunctionDefinition, FunctionDefinitionType> resolver) {
        super(functions);
        this.resolver = resolver;
    }

    public synchronized
    @NotNull FunctionGroup tokenizeDefinition(@NotNull FunctionDefinition definition,
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
                                     FunctionDefinition definition) {
        int split = definition.getDefinitionSpliterator();
        MutableSymbol<? extends Number> variable = definition.getVariable();

        if (!buffer.context().contains(variable.getName())) {
            setContextParent(buffer.context());
            buffer.context().addSymbol(variable);
        }

        buffer.iterator().setCursor(split);

        return new SimpleToken(
                DeclarationType.DECLARATION,
                definition.getString().substring(0, split),
                0
        );
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
    Result findFromContext(@Mutates Context<?> context,
                           CharSequence symbols,
                           int start) {
        setContextParent(context);
        Optional<? extends MutableSymbol<?>> optional = context.getSymbol(symbols.toString());
        MutableSymbol<?> symbol;

        if (optional.isPresent()) {
            symbol = optional.get();
        } else {
            symbol = getDefaultFactory(symbols, getDefaultValue()).get();
            context.getParent().addSymbol(symbol);
        }

        Token token = new SimpleToken(symbol.type(), symbols, start);
        return new Result(true, token);
    }

    private void setContextParent(Context<?> context) {
        if (context.getParent() == null) {
            context.setParent(new NamingContext<>());
        }
    }

    @Override
    protected Result find(CharSequence symbols, int start) {
        Result result = super.find(symbols, start);

        return "=".contentEquals(symbols)
                ? Result.reduce(result, new Result(
                    true,
                    new SimpleToken(DeclarationType.DECLARATION_TERMINAL, symbols, start)))
                : result;
    }
}
