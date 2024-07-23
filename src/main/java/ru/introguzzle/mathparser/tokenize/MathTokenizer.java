package ru.introguzzle.mathparser.tokenize;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.introguzzle.mathparser.common.Nameable;
import ru.introguzzle.mathparser.common.NoSuchNameException;
import ru.introguzzle.mathparser.definition.FunctionDefinition;
import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.expression.ExpressionIterator;
import ru.introguzzle.mathparser.function.AbstractFunction;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.symbol.*;
import ru.introguzzle.mathparser.constant.Constant;
import ru.introguzzle.mathparser.constant.ConstantReflector;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.function.FunctionReflector;
import ru.introguzzle.mathparser.tokenize.token.*;

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

    public MathTokenizer() {
        nameableMap.putAll(FunctionReflector.get());
        nameableMap.putAll(ConstantReflector.get());
    }

    public MathTokenizer(@NotNull Map<String, ? extends Function> functions) {
        nameableMap.putAll(functions);
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

    public MathTokenizer overrideFunction(@NotNull String name,
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

    public MathTokenizer overrideConstant(@NotNull String name, double value) {
        nameableMap.remove(name);
        nameableMap.put(name, new Constant(name, value) {});
        return this;
    }

    public MathTokenizer addFunction(@NotNull Function function) {
        nameableMap.put(function.getName(), function);
        return this;
    }

    public MathTokenizer addConstant(@NotNull Constant constant) {
        nameableMap.put(constant.getName(), constant);
        return this;
    }

    public MathTokenizer addName(@NotNull Nameable nameable) {
        nameableMap.put(nameable.getName(), nameable);
        return this;
    }


    public MathTokenizer clearFunctions() {
        nameableMap.entrySet()
                .removeIf(entry -> entry.getValue() instanceof Function);

        return this;
    }

    public MathTokenizer clearConstants() {
        nameableMap.entrySet()
                .removeIf(entry -> entry.getValue() instanceof ImmutableSymbol);

        return this;
    }

    @Override
    public synchronized @NotNull Tokens tokenize(@NotNull Expression expression, @NotNull Context context) throws TokenizeException {
        Stack<Character> parenthesisStack = new Stack<>();
        Tokens tokens = new SimpleTokens();
        ExpressionIterator iterator = expression.iterator();
        Buffer buffer = new Buffer(iterator, expression, context);

        if (expression.getString().isBlank() || expression.getString().isEmpty()) {
            return new SimpleTokens(new SimpleToken(TokenType.EOF, " ", 0));
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
                    tokens.add(TokenType.LEFT_PARENTHESIS, current, iterator.getCursor());
                    parenthesisStack.push(current);
                    iterator.next();
                    continue;
                case ')':
                    tokens.add(TokenType.RIGHT_PARENTHESIS, current, iterator.getCursor());
                    if (parenthesisStack.isEmpty()) {
                        throw new IllegalBracketStartException(expression, iterator.getCursor());
                    } else {
                        parenthesisStack.pop();
                    }

                    iterator.next();
                    continue;
                case '+':
                case '-':
                case '*':
                case '/':
                case '^':
                case '>':
                case '<':
                case '=':
                case '!':
                case '&':
                case '|':
                case '~':
                    tokens.add(handleOperator(buffer));
                    if (!iterator.hasNext()) {
                        break;
                    }

                    iterator.next();
                    continue;
                case ',':
                    tokens.add(TokenType.COMMA, current, iterator.getCursor());
                    iterator.next();
                    continue;
                default:
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

        tokens.add(TokenType.EOF, "", iterator.getCursor());
        return tokens;
    }

    @Override
    public List<ImmutableSymbol> getConstants() {
        return nameableMap.values()
                .stream()
                .filter(n -> n instanceof ImmutableSymbol)
                .map(nameable -> (ImmutableSymbol) nameable)
                .collect(Collectors.toList());
    }

    @Override
    public List<Function> getFunctions() {
        return nameableMap.values()
                .stream()
                .filter(n -> n instanceof Function)
                .map(Function.class::cast)
                .toList();
    }

    protected Token handleDefinition(Buffer buffer, FunctionDefinition definition) {
        int split = definition.getDefinitionSpliterator();
        Variable variable = definition.getVariable();

        if (!buffer.context.contains(variable.getName())) {
            buffer.context.addSymbol(variable);
        }

        buffer.iterator.setCursor(split);
        return new SimpleToken(
                TokenType.DECLARATION,
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

        return new SimpleToken(TokenType.NUMBER, number, start);
    }

    protected Token handleOperator(Buffer buffer)
            throws FunctionDefinitionException, UnknownOperatorException {
        ExpressionIterator iterator = buffer.iterator;
        Character current = iterator.current();
        Character next = iterator.peekNext();

        final int offset = iterator.getCursor();

        return switch (current) {
            case '+' -> new SimpleToken(TokenType.OPERATOR_ADD, current, offset);
            case '-' -> new SimpleToken(TokenType.OPERATOR_SUB, current, offset);
            case '*' -> {
                if (next != null && next == '*') {
                    iterator.next();
                    yield new SimpleToken(TokenType.OPERATOR_EXP, "**", offset);
                }

                yield new SimpleToken(TokenType.OPERATOR_MUL, current, offset);
            }
            case '/' -> new SimpleToken(TokenType.OPERATOR_DIV, current, offset);
            case '^' -> new SimpleToken(TokenType.OPERATOR_XOR, current, offset);
            case '&' -> new SimpleToken(TokenType.OPERATOR_AND, current, offset);
            case '|' -> new SimpleToken(TokenType.OPERATOR_OR, current, offset);
            case '~' -> new SimpleToken(TokenType.OPERATOR_BITWISE_NOT, current, offset);
            case '<' -> {
                if (next != null && next == '<') {
                    iterator.next();
                    yield new SimpleToken(TokenType.OPERATOR_LEFT_SHIFT, "<<", offset);
                } else if (iterator.peekNext() == '=') {
                    iterator.next();
                    yield new SimpleToken(TokenType.OPERATOR_LESS_OR_EQUALS, "<=", offset);
                }

                yield new SimpleToken(TokenType.OPERATOR_LESS, current, offset);
            }
            case '>' -> {
                if (next != null && next == '>') {
                    iterator.next();
                    yield new SimpleToken(TokenType.OPERATOR_RIGHT_SHIFT, ">>", offset);

                } else if (next != null && next == '=') {
                    iterator.next();
                    yield new SimpleToken(TokenType.OPERATOR_GREATER_OR_EQUALS, ">=", offset);
                }

                yield new SimpleToken(TokenType.OPERATOR_GREATER, current, offset);
            }
            case '=' -> {
                if (next != null && next == '=') {
                    iterator.next();
                    yield new SimpleToken(TokenType.OPERATOR_EQUALS, "==", offset);
                }

                if (buffer.expression instanceof FunctionDefinition) {
                    yield new SimpleToken(TokenType.DECLARATION_END, current, offset);
                } else {
                    throw new FunctionDefinitionException("Declaration is not allowed", buffer.expression, offset);
                }
            }
            case '!' -> {
                if (next != null && next == '=') {
                    iterator.next();
                    yield new SimpleToken(TokenType.OPERATOR_NOT_EQUALS, "!=", offset);
                }

                yield new SimpleToken(TokenType.OPERATOR_NOT, "!", offset);
            }

            default -> throw new UnknownOperatorException(current, buffer.expression, offset);
        };
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
                    result.token = new SimpleToken(s.type(), symbols, start);
                    result.match = true;
                });

        return result;
    }

    protected Result findFromContext(Context context, CharSequence symbols, int start) {
        Result result = new Result();

        context.getSymbols()
                .stream()
                .parallel()
                .filter(s -> s.getName().contentEquals(symbols))
                .findFirst()
                .ifPresent(s -> {
                    result.token = new SimpleToken(s.type(), symbols, start);
                    result.match = true;
                });

        return result;
    }
}
