package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.introguzzle.mathparser.common.*;
import ru.introguzzle.mathparser.definition.FunctionDefinition;
import ru.introguzzle.mathparser.expression.ExpressionIterator;
import ru.introguzzle.mathparser.function.AbstractFunction;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.operator.Operator;
import ru.introguzzle.mathparser.operator.OperatorReflector;
import ru.introguzzle.mathparser.symbol.*;
import ru.introguzzle.mathparser.constant.Constant;
import ru.introguzzle.mathparser.constant.ConstantReflector;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.function.FunctionReflector;
import ru.introguzzle.mathparser.tokenize.token.*;
import ru.introguzzle.mathparser.tokenize.token.type.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class MathTokenizer implements Tokenizer, Serializable {

    private TokenizerOptions options = new TokenizerOptions() {};

    @Override
    public void setOptions(TokenizerOptions options) {
        this.options = options;
    }

    @Override
    public TokenizerOptions getOptions() {
        return options;
    }

    public static class Result {
        public boolean match = false;
        public Token token;

        public Result() {

        }

        public Result(boolean match, @Nullable Token token) {
            this.match = match;
            this.token = token;
        }

        @Contract("_ -> new")
        public static @NotNull Result reduce(Result... results) {
            boolean match = Arrays.stream(results).anyMatch(r -> r.match);

            if (!match) {
                return new Result(false, null);
            }

            Token nonNullToken = Arrays.stream(results)
                    .sequential()
                    .map(result -> result.token)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);

            return new Result(true, nonNullToken);
        }
    }

    public record Buffer(@NotNull ExpressionIterator iterator,
                         @NotNull Expression expression,
                         @NotNull Context context) {
    }

    @Serial
    private static final long serialVersionUID = -54943912839L;

    @NotNull
    private final transient Map<String, Nameable> nameableMap = new HashMap<>();

    private String allowedOperatorSymbols;

    public MathTokenizer setAllowedOperatorSymbols(String allowedOperatorSymbols) {
        this.allowedOperatorSymbols = allowedOperatorSymbols;
        return this;
    }

    public MathTokenizer() {
        nameableMap.putAll(FunctionReflector.get());
        nameableMap.putAll(ConstantReflector.get());
        nameableMap.putAll(OperatorReflector.get());
    }

    public MathTokenizer(@NotNull Map<String, ? extends Function> functions) {
        nameableMap.putAll(functions);
        nameableMap.putAll(ConstantReflector.get());
        nameableMap.putAll(OperatorReflector.get());
    }

    private static Map<String, Nameable> toMap(@NotNull Collection<? extends Nameable> collection) {
        return collection.stream().collect(Collectors.toMap(Nameable::getName, n -> n));
    }

    public MathTokenizer withFunctions(@NotNull Collection<? extends Function> functions) {
        nameableMap.putAll(toMap(functions));
        return this;
    }

    public MathTokenizer withConstants(@NotNull Collection<? extends ImmutableSymbol> constants) {
        nameableMap.putAll(toMap(constants));
        return this;
    }

    public MathTokenizer withOperators(@NotNull Collection<? extends Operator> operators) {
        nameableMap.putAll(toMap(operators));
        return this;
    }

    public
    MathTokenizer overrideFunction(@NotNull String name,
                                   int requiredArguments,
                                   boolean variadic,
                                   @NotNull java.util.function.Function<List<Double>, Double> replace) {
        if (!nameableMap.containsKey(name)) {
            throw new NoSuchNameException(name, nameableMap.keySet());
        }

        Function newFunction = new AbstractFunction(name, requiredArguments) {
            @Override
            public Double apply(List<Double> arguments) {
                return replace.apply(arguments);
            }

            @Override
            public boolean isVariadic() {
                return variadic;
            }
        };

        nameableMap.replace(name, newFunction);
        return this;
    }

    public
    MathTokenizer overrideOperator(@NotNull String name,
                                   Operator operator) {
        if (!nameableMap.containsKey(name)) {
            throw new NoSuchNameException(name, nameableMap.keySet());
        }

        nameableMap.replace(name, operator);
        return this;
    }

    public MathTokenizer overrideConstant(@NotNull String name, double value) {
        nameableMap.remove(name);
        nameableMap.put(name, new Constant(name, value) {});
        return this;
    }

    public MathTokenizer addFunction(@NotNull Function function) {
        addName(function);
        return this;
    }

    public MathTokenizer addConstant(@NotNull Constant constant) {
        addName(constant);
        return this;
    }

    public MathTokenizer addName(@NotNull Nameable nameable) {
        nameableMap.put(nameable.getName(), nameable);
        return this;
    }

    public MathTokenizer addOperator(@NotNull Operator operator) {
        addName(operator);
        return this;
    }

    private <X extends Nameable> MathTokenizer clearNameables(Class<X> cls) {
        nameableMap.entrySet()
                .removeIf(cls::isInstance);
        return this;
    }

    public MathTokenizer clearFunctions() {
        return clearNameables(Function.class);
    }

    public MathTokenizer clearConstants() {
        return clearNameables(Constant.class);
    }

    public MathTokenizer clearOperators() {
        return clearNameables(Operator.class);
    }

    @Override
    public synchronized @NotNull Tokens tokenize(@NotNull Expression expression, @NotNull Context context) throws TokenizeException {
        Stack<Character> parenthesisStack = new Stack<>();
        Tokens tokens = new SimpleTokens();
        ExpressionIterator iterator = expression.iterator();
        Buffer buffer = new Buffer(iterator, expression, context);

        if (expression.getString().isBlank() || expression.getString().isEmpty()) {
            return new SimpleTokens(new SimpleToken(TerminalType.TERMINAL, " ", 0));
        }

        if (expression instanceof FunctionDefinition definition) {
            Token declarationToken = handleDefinition(buffer, definition);
            tokens.add(declarationToken);
        }

        while (iterator.hasNext()) {
            char current = iterator.current();
            switch (current) {
                case ' ':
                    iterator.next();
                    continue;

                case '(':
                    tokens.add(ParenthesisType.LEFT, current, iterator.getCursor());
                    parenthesisStack.push(current);
                    iterator.next();
                    continue;

                case ')':
                    tokens.add(ParenthesisType.RIGHT, current, iterator.getCursor());
                    if (parenthesisStack.isEmpty()) {
                        throw new IllegalBracketStartException(expression, iterator.getCursor());
                    } else {
                        parenthesisStack.pop();
                    }

                    iterator.next();
                    continue;

                case ',':
                    tokens.add(SpecialType.COMMA, current, iterator.getCursor());
                    iterator.next();
                    continue;

                default:
                    if (iterator.isSpecial(allowedOperatorSymbols)) {
                        tokens.add(handleOperator(buffer));
                        if (!iterator.hasNext()) {
                            break;
                        }

                        continue;
                    }

                    if (iterator.isDigit()) {
                        tokens.add(handleNumber(buffer));
                        if (!iterator.hasNext()) {
                            break;
                        }

                        continue;
                    }

                    if (iterator.isLetter()) {
                        tokens.add(handleSymbols(buffer));
                        if (!iterator.hasNext()) {
                            break;
                        }

                        continue;
                    }

                    throw new UnknownCharacterException(current, expression, iterator.getCursor());
            }
        }

        if (!parenthesisStack.isEmpty()) {
            throw new BracketMismatchException(expression, iterator.getCursor());
        }

        tokens.add(TerminalType.TERMINAL, "", iterator.getCursor());
        return tokens;
    }

    private <X extends Nameable> List<X> getFromNameables(Class<X> cls) {
        return nameableMap.values()
                .stream()
                .filter(cls::isInstance)
                .map(cls::cast)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull List<ImmutableSymbol> getConstants() {
        return getFromNameables(ImmutableSymbol.class);
    }

    @Override
    public @NotNull List<Function> getFunctions() {
        return getFromNameables(Function.class);
    }

    public @NotNull List<Operator> getOperators() {
        return getFromNameables(Operator.class);
    }

    protected Token handleDefinition(Buffer buffer, FunctionDefinition definition) {
        int split = definition.getDefinitionSpliterator();
        Variable variable = definition.getVariable();

        if (!buffer.context.contains(variable.getName())) {
            buffer.context.addSymbol(variable);
        }

        buffer.iterator.setCursor(split);
        return new SimpleToken(
                DeclarationType.DECLARATION,
                definition.getString().substring(split),
                0
        );
    }

    protected Token handleNumber(Buffer buffer) {
        StringBuilder number = new StringBuilder();

        ExpressionIterator iterator = buffer.iterator;
        char current = iterator.current();
        final int start = iterator.getCursor();

        while (iterator.hasNext() && iterator.isDigit()) {
            number.append(current);
            iterator.next();
            if (!iterator.hasNext()) {
                break;
            }

            current = iterator.current();
        }

        return new SimpleToken(NumberType.NUMBER, number, start);
    }

    protected Token handleOperator(Buffer buffer)
            throws UnknownOperatorException {
        ExpressionIterator iterator = buffer.iterator;
        StringBuilder operator = new StringBuilder();
        final int start = iterator.getCursor();

        while (iterator.hasNext() && iterator.isSpecial(allowedOperatorSymbols)) {
            operator.append(iterator.next());
            if (!iterator.hasNext()) {
                break;
            }
        }

        Result operatorResult = find(operator, start);

        if (operatorResult.token == null) {
            if ("=".contentEquals(operator)) {
                return new SimpleToken(DeclarationType.DECLARATION_TERMINAL, operator, start);
            }

            throw new UnknownOperatorException(operator, buffer.expression, start);
        }

        return operatorResult.token;
    }

    protected Token handleSymbols(Buffer buffer) throws TokenizeException {
        ExpressionIterator iterator = buffer.iterator;
        StringBuilder symbols = new StringBuilder();
        final int start = iterator.getCursor();

        while (iterator.hasNext() && iterator.isLetter()) {
            symbols.append(iterator.next());
            if (!iterator.hasNext()) {
                break;
            }
        }

        Result result = Result.reduce(
                find(symbols, start),
                findFromContext(buffer.context, symbols, start)
        );

        if (!result.match) {
            iterator.setCursor(start);
            throw new UnknownSymbolTokenizeException(symbols, buffer.expression, start);
        }

        return result.token;
    }

    /**
     *
     * @param symbols Sequence of letters (candidate for nameable)
     * @return Search result
     */

    protected Result find(CharSequence symbols, int start) {
        Result result = new Result();

        nameableMap.values().stream()
                .filter(Nameable.match(symbols))
                .findFirst()
                .ifPresent(s -> {
                    result.token = s.getToken(start);
                    result.match = true;
                });

        return result;
    }

    protected Result findFromContext(Context context, CharSequence symbols, int start) {
        Result result = new Result();

        context.getSymbol(symbols.toString())
                .ifPresent(s -> {
                    result.token = s.getToken(start);
                    result.match = true;
                });

        return result;
    }
}
