package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.introguzzle.mathparser.common.*;
import ru.introguzzle.mathparser.expression.ExpressionIterator;
import ru.introguzzle.mathparser.function.real.DoubleFunction;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.function.real.DoubleFunctionReflector;
import ru.introguzzle.mathparser.group.Group;
import ru.introguzzle.mathparser.group.TokenGroup;
import ru.introguzzle.mathparser.operator.Operator;
import ru.introguzzle.mathparser.operator.DoubleOperatorReflector;
import ru.introguzzle.mathparser.symbol.*;
import ru.introguzzle.mathparser.constant.real.DoubleConstant;
import ru.introguzzle.mathparser.constant.real.DoubleConstantReflector;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.tokenize.token.*;
import ru.introguzzle.mathparser.tokenize.token.type.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MathTokenizer implements Tokenizer, Serializable {
    private static final char DECIMAL = '.';
    private static final char IMAGINARY_UNIT = 'i';
    private static final char UNDERSCORE = '_';

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
                         @NotNull Context<?> context) {
    }

    @Serial
    private static final long serialVersionUID = -54943912839L;

    @NotNull
    private final transient Map<String, Nameable> names = new HashMap<>();

    private final Map<String, Operator<?>> operators = new HashMap<>();
    private final Map<String, Function<?>> functions = new HashMap<>();
    private final Map<String, ImmutableSymbol<?>> constants = new HashMap<>();

    private String allowedOperatorSymbols = "+-/*~!@#$%^&*()\"{}_[]|\\?/<>,.=";
    private Predicate<Character> digitPredicate = c ->
            c != null && (c == DECIMAL || c == IMAGINARY_UNIT || Character.isDigit(c));

    private Predicate<Character> letterPredicate = c ->
            c != null && (c == UNDERSCORE || Character.isLetter(c));

    public MathTokenizer setAllowedOperatorSymbols(String allowedOperatorSymbols) {
        this.allowedOperatorSymbols = allowedOperatorSymbols;
        return this;
    }

    public MathTokenizer setDigitPredicate(Predicate<Character> digitPredicate) {
        this.digitPredicate = digitPredicate;
        return this;
    }

    public MathTokenizer setLetterPredicate(Predicate<Character> letterPredicate) {
        this.letterPredicate = letterPredicate;
        return this;
    }

    public MathTokenizer() {
        names.putAll(DoubleFunctionReflector.get());
        names.putAll(DoubleConstantReflector.get());
        names.putAll(DoubleOperatorReflector.get());
    }

    public MathTokenizer(@NotNull Map<String, ? extends Function<?>> functions) {
        this();
        names.putAll(functions);
    }

    private static Map<String, Nameable> toMap(@NotNull Collection<? extends Nameable> collection) {
        return collection.stream().collect(Collectors.toMap(Nameable::getName, n -> n));
    }

    public MathTokenizer withFunctions(@NotNull Collection<? extends Function<?>> functions) {
        names.putAll(toMap(functions));
        return this;
    }

    public MathTokenizer withConstants(@NotNull Collection<? extends ImmutableSymbol<?>> constants) {
        names.putAll(toMap(constants));
        return this;
    }

    public MathTokenizer withOperators(@NotNull Collection<? extends Operator<?>> operators) {
        names.putAll(toMap(operators));
        return this;
    }

    public
    MathTokenizer overrideFunction(@NotNull String name,
                                   int requiredArguments,
                                   boolean variadic,
                                   @NotNull java.util.function.Function<List<Double>, Double> replace) {
        if (!names.containsKey(name)) {
            throw new NoSuchNameException(name, names.keySet());
        }

        Function<Double> newFunction = new DoubleFunction(name, requiredArguments) {
            @Override
            public Double apply(List<Double> arguments) {
                return replace.apply(arguments);
            }

            @Override
            public boolean isVariadic() {
                return variadic;
            }
        };

        names.replace(name, newFunction);
        return this;
    }

    public
    MathTokenizer overrideOperator(@NotNull String name,
                                   Operator<?> operator) {
        if (!names.containsKey(name)) {
            throw new NoSuchNameException(name, names.keySet());
        }

        names.replace(name, operator);
        return this;
    }

    public MathTokenizer overrideConstant(@NotNull String name, double value) {
        names.remove(name);
        names.put(name, new DoubleConstant(name, value) {});
        return this;
    }

    public MathTokenizer addFunction(@NotNull Function<?> function) {
        addName(function);
        return this;
    }

    public MathTokenizer addConstant(@NotNull DoubleConstant constant) {
        addName(constant);
        return this;
    }

    public MathTokenizer addName(@NotNull Nameable nameable) {
        names.put(nameable.getName(), nameable);
        return this;
    }

    public MathTokenizer addOperator(@NotNull Operator<?> operator) {
        addName(operator);
        return this;
    }

    private <N extends Nameable> MathTokenizer clearNameables(Class<N> cls) {
        names.entrySet()
                .removeIf(cls::isInstance);
        return this;
    }

    public MathTokenizer clearFunctions() {
        return clearNameables(Function.class);
    }

    public MathTokenizer clearConstants() {
        return clearNameables(DoubleConstant.class);
    }

    public MathTokenizer clearOperators() {
        return clearNameables(Operator.class);
    }

    @Override
    public synchronized
    @NotNull Group tokenize(@NotNull Expression expression,
                            @NotNull Context<?> context) throws TokenizeException {
        return new TokenGroup(start(new Buffer(expression.iterator(), expression, context)));
    }

    @Override
    public @NotNull Map<String, Nameable> getNames() {
        return names;
    }

    protected @NotNull Tokens start(@NotNull Buffer buffer) throws TokenizeException {
        Stack<Character> parenthesisStack = new Stack<>();
        Tokens tokens = new SimpleTokens();
        ExpressionIterator iterator = buffer.iterator;
        Expression expression = buffer.expression;

        if (expression.getString().isBlank() || expression.getString().isEmpty()) {
            return new SimpleTokens(new SimpleToken(TerminalType.TERMINAL, " ", 0));
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

                    if (iterator.isDigit(digitPredicate)) {
                        tokens.add(handleNumber(buffer));
                        if (!iterator.hasNext()) {
                            break;
                        }

                        continue;
                    }

                    if (iterator.isLetter(letterPredicate)) {
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

    @Override
    public @NotNull Map<String, Operator<?>> getOperators() {
        if (!operators.isEmpty()) {
            return operators;
        }

        names.forEach((k, v) -> {
            if (v instanceof Operator<?>) {
                operators.put(k, (Operator<?>) v);
            }
        });

        return operators;
    }

    @Override
    public @NotNull Map<String, Function<?>> getFunctions() {
        if (!functions.isEmpty()) {
            return functions;
        }

        names.forEach((k, v) -> {
            if (v instanceof Function<?>) {
                functions.put(k, (Function<?>) v);
            }
        });

        return functions;
    }

    @Override
    public @NotNull Map<String, ImmutableSymbol<?>> getConstants() {
        if (!constants.isEmpty()) {
            return constants;
        }

        names.forEach((k, v) -> {
            if (v instanceof ImmutableSymbol<?>) {
                constants.put(k, (ImmutableSymbol<?>) v);
            }
        });

        return constants;
    }

    protected Token handleNumber(Buffer buffer) throws InvalidNumberFormatException {
        StringBuilder number = new StringBuilder();

        ExpressionIterator iterator = buffer.iterator;
        char current = iterator.current();
        final int start = iterator.getCursor();

        int imaginaryUnitCount = 0;
        int decimalPointCount = 0;

        while (iterator.hasNext() && iterator.isDigit(digitPredicate)) {
            if (current == IMAGINARY_UNIT) {
                imaginaryUnitCount++;
            }

            if (current == DECIMAL) {
                decimalPointCount++;
            }

            if (imaginaryUnitCount > 1 || decimalPointCount > 1) {
                throw new InvalidNumberFormatException(number, buffer.expression, iterator.getCursor());
            }

            number.append(current);
            iterator.next();
            if (!iterator.hasNext()) {
                break;
            }

            current = iterator.current();
        }

        if (imaginaryUnitCount > 0) {
            return new SimpleToken(NumberType.COMPLEX_NUMBER, number, start);
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
            throw new UnknownOperatorException(operator, buffer.expression, start);
        }

        return operatorResult.token;
    }

    protected Token handleSymbols(Buffer buffer) throws TokenizeException {
        ExpressionIterator iterator = buffer.iterator;
        StringBuilder symbols = new StringBuilder();
        final int start = iterator.getCursor();

        while (iterator.hasNext() && iterator.isLetter(letterPredicate)) {
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

        names.values().stream()
                .filter(Nameable.match(symbols))
                .findFirst()
                .ifPresent(s -> {
                    result.token = s.getToken(start);
                    result.match = true;
                });

        return result;
    }

    protected Result findFromContext(Context<?> context, CharSequence symbols, int start) {
        Result result = new Result();

        context.getSymbol(symbols.toString())
                .ifPresent(s -> {
                    result.token = s.getToken(start);
                    result.match = true;
                });

        return result;
    }
}
