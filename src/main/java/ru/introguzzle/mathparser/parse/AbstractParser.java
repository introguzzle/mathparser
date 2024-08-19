package ru.introguzzle.mathparser.parse;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Nameable;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.common.naming.Context;
import ru.introguzzle.mathparser.common.naming.NamingContext;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.lambda.LambdaEvaluator;
import ru.introguzzle.mathparser.operator.Operator;
import ru.introguzzle.mathparser.operator.standard.AdditionOperator;
import ru.introguzzle.mathparser.operator.standard.SubtractionOperator;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;
import ru.introguzzle.mathparser.symbol.LambdaArgument;
import ru.introguzzle.mathparser.tokenize.TokenizeException;
import ru.introguzzle.mathparser.tokenize.Tokenizer;
import ru.introguzzle.mathparser.tokenize.UnknownSymbolTokenizeException;
import ru.introguzzle.mathparser.tokenize.token.CompositeToken;
import ru.introguzzle.mathparser.tokenize.token.NumberToken;
import ru.introguzzle.mathparser.tokenize.token.Token;
import ru.introguzzle.mathparser.tokenize.token.Tokens;
import ru.introguzzle.mathparser.tokenize.token.type.*;
import ru.introguzzle.mathparser.unit.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An implementation of {@link Parser<T>} that provides a basic framework for parsing mathematical expressions.
 * It handles basic operations like negation and addition and delegates specific
 * parsing and evaluation responsibilities to its subclasses.
 *
 * @param <T> The type of number that this parser handles (e.g., Double, Complex).
 * @see Parser<T>
 */
public abstract class AbstractParser<T extends Number> implements Parser<T> {
    /**
     * Special operator to handle unary minus (negation).
     */
    static final Operator<?> SPECIAL_UNARY_MINUS = new SubtractionOperator();

    /**
     * Special operator to handle unary plus.
     */
    static final Operator<?> SPECIAL_UNARY_PLUS = new AdditionOperator();

    /**
     * The tokenizer used by this parser to tokenize the expression.
     * This field is final, meaning it is initialized once and cannot be changed.
     */
    private final Tokenizer tokenizer;

    /**
     * Returns the class type of the LambdaEvaluator associated with this parser.
     *
     * @return The LambdaEvaluator class type.
     */
    protected abstract @NotNull Class<? extends LambdaEvaluator<T>> getLambdaClass();

    /**
     * Returns the class type of the Operator associated with this parser.
     *
     * @return The Operator class type.
     */
    protected abstract @NotNull Class<? extends Operator<T>> getOperatorClass();

    /**
     * Returns the class type of the Function associated with this parser.
     *
     * @return The Function class type.
     */
    protected abstract @NotNull Class<? extends Function<T>> getFunctionClass();

    /**
     * Returns the class type of the ImmutableSymbol associated with this parser.
     *
     * @return The ImmutableSymbol class type.
     */
    protected abstract @NotNull Class<? extends ImmutableSymbol<T>> getSymbolClass();

    /**
     * Casts a Nameable object to a specific class type.
     *
     * @param cls The class type to cast to.
     * @param o The object to cast.
     * @param <U> The target class type.
     * @param <N> The source class type.
     * @return The cast object.
     * @throws UnsupportedOperationException if the object cannot be cast to the specified class.
     */
    private static <U extends Nameable, N extends Nameable> U cast(Class<U> cls, N o) {
        if (!cls.isInstance(o)) {
            String format = "Cannot cast %s to class %s";
            String message = String.format(format, o.describe(), cls.getSimpleName());
            throw new UnsupportedOperationException(message);
        }

        return cls.cast(o);
    }

    public AbstractParser<T> addUnit(Unit<?, ?> unit) {
        getTokenizer().getOptions().addUnit(unit);
        return this;
    }

    // Standard operations such as negation and addition

    /**
     * Compares two values.
     *
     * @param left The left operand.
     * @param right The right operand.
     * @return {@code true} if the left operand is greater than the right operand, {@code false} otherwise.
     */
    public abstract boolean compare(T left, T right);

    /**
     * Returns the value that represents the absence of a value in the parser's context.
     * <br>
     * This operation is optional and depends on whether the type {@code T} supports such an operation.
     * <br>
     * If the type {@code T} does not support this operation, calling this method may throw a {@code SyntaxException}.
     *
     * @return The absent value.
     * @throws SyntaxException If the operation is not supported by {@code T} or if an error occurs during parsing.
     */
    public abstract T absentValue() throws SyntaxException;

    /**
     * Negates a given value.
     * <br>
     * This operation is optional and depends on whether the type {@code T} supports such an operation.
     * <br>
     * If the type {@code T} does not support this operation, calling this method may throw a {@code SyntaxException}.
     *
     * @param value The value to negate.
     * @return The negated value.
     * @throws SyntaxException If the operation is not supported by {@code T} or if an error occurs during parsing.
     */
    public abstract T negateValue(T value) throws SyntaxException;

    /**
     * Adds two values.
     * <br>
     * This operation is optional and depends on whether the type {@code T} supports such an operation.
     * <br>
     * If the type {@code T} does not support this operation, calling this method may throw a {@code SyntaxException}.
     *
     * @param left The left operand.
     * @param right The right operand.
     * @return The sum of the left and right operands.
     * @throws SyntaxException If the operation is not supported by {@code T} or if an error occurs during parsing.
     */
    public abstract T add(T left, T right) throws SyntaxException;

    /**
     * Parses a unit expression from the given tokens and context.
     *
     * @param value The value to which the unit conversion applies.
     * @param tokens The tokens representing the expression.
     * @param context The context in which the expression is parsed.
     * @return The value after applying the unit conversion.
     * @throws SyntaxException If an error occurs during parsing.
     */
    public abstract T parseUnit(T value, Tokens tokens, Context<T> context) throws SyntaxException;

    public AbstractParser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public Tokenizer getTokenizer() {
        return tokenizer;
    }

    protected Tokens tokenize(Expression expression, Context<T> context) throws TokenizeException {
        return tokenizer.tokenize(expression, context).getTokens();
    }

    @Override
    public T parse(@NotNull Expression expression) throws SyntaxException {
        return parse(expression, new NamingContext<>());
    }

    @Override
    public T parse(@NotNull Expression expression, @NotNull Context<T> context) throws SyntaxException {
        Tokens tokens = tokenize(expression, context);
        return parse(tokens, context);
    }

    @Override
    public T parse(@NotNull Tokens tokens, Context<T> context) throws SyntaxException {
        Token token = tokens.next();

        if (token.getType().isTerminal()) {
            return absentValue();
        }

        tokens.back();
        return parseExpression(tokens, context, Integer.MAX_VALUE);
    }

    protected T parseExpression(Tokens tokens, Context<T> context, int priority) throws SyntaxException {
        T leftValue = parseFactor(tokens, context);

        while (true) {
            Token token = tokens.next();
            if (token.getType() == UnitType.UNIT) {
                return parseUnit(leftValue, tokens, context);
            }

            Optional<Operator<?>> optional = tokenizer.getOptions().findOperator(token.getData());

            if (optional.isEmpty() || !(getOperatorClass().isInstance(optional.get())) || optional.get().getPriority() >= priority) {
                tokens.back();
                break;
            }

            Operator<T> operator = cast(getOperatorClass(), optional.get());

            int nextPriority = operator.isRightAssociative()
                    ? operator.getPriority() + 1
                    : operator.getPriority();

            T rightValue = parseExpression(tokens, context, nextPriority);
            leftValue = operator.apply(List.of(leftValue, rightValue));
        }

        return leftValue;
    }

    protected T parseFactor(Tokens tokens, Context<T> context) throws SyntaxException {
        Token token = tokens.next();

        Type type;
        switch (type = token.getType()) {
            case FunctionType.FUNCTION:
                tokens.back();
                return parseFunction(tokens, context);

            case OperatorType.OPERATOR:
                if (token.getData().equals(SPECIAL_UNARY_MINUS.getName())) {
                    return negateValue(parseFactor(tokens, context));
                }

                if (token.getData().equals(SPECIAL_UNARY_PLUS.getName())) {
                    return parseFactor(tokens, context);
                }

                Operator<T> operator = cast(getOperatorClass(), tokenizer.getOptions().findOperator(token.getData()).orElseThrow());
                return operator.apply(List.of(parseFactor(tokens, context)));

            case NumberType.NUMBER:
                assert token instanceof NumberToken;

                NumberToken numberToken = (NumberToken) token;
                String plain = numberToken.getNumber().getPlain();
                return getConverter().convert(plain);

            case SymbolType.CONSTANT:
                Optional<ImmutableSymbol<?>> symbol = tokenizer.getOptions().findConstant(token.getData());
                if (symbol.isPresent()) {
                    return cast(getSymbolClass(), symbol.get()).getValue();
                }

                throw new UnexpectedTokenException(tokens, token);

            case SymbolType.VARIABLE:
            case SymbolType.COEFFICIENT:
            case SymbolType.LAMBDA_ARGUMENT:
                Token finalToken = token;
                return context
                        .getSymbol(token.getData())
                        .orElseThrow(() -> new UnknownSymbolTokenizeException(finalToken.getData(), tokens.toExpression(), tokens.get(tokens.getPosition()).getOffset()))
                        .getValue();

            case ParenthesisType.LEFT:
                T value = parse(tokens, context);
                token = tokens.next();
                if (token.getType() != ParenthesisType.RIGHT) {
                    throw new UnexpectedTokenException(tokens, token);
                }

                return value;

            default:
                if (type.getCategory() == Type.Category.LAMBDA) {
                    if (token instanceof CompositeToken compositeToken) {
                        return parseLambdaFunction(compositeToken.getTokens(), context);
                    }
                }

                throw new UnexpectedTokenException(tokens, token);
        }
    }

    protected T parseLambdaFunction(Tokens tokens, Context<T> context) throws SyntaxException {
        tokens.reset();
        String name = tokens.next().getData();

        LambdaEvaluator<T> lambdaEvaluator = cast(getLambdaClass(), getTokenizer().getOptions().findLambdaEvaluator(name).orElseThrow());

        tokens.next();

        List<T> arguments = new ArrayList<>();
        List<LambdaArgument<T>> lambdaArguments = new ArrayList<>();
        Token token;

        try {
            token = tokens.next();
        } catch (IndexOutOfBoundsException e) {
            int offset = tokens.getPosition() < tokens.size()
                    ? tokens.getPosition()
                    : tokens.size() - 1;

            throw new UnexpectedTokenException(tokens, offset);
        }

        for (Token t: tokens) {
            if (t.getType() == SymbolType.LAMBDA_ARGUMENT) {
                if (!context.contains(t.getData())) {
                    LambdaArgument<T> lambdaArgument = new LambdaArgument<>(t.getData(), absentValue());
                    context.addSymbol(lambdaArgument);
                    lambdaArguments.add(lambdaArgument);
                }
            }
        }

        int index = 0;
        int lambdaIndex = 0;

        List<Tokens> split = tokens.split(DelimiterType.COMMA);
        for (int i = 0; i < split.size(); i++) {
            for (Token t : split.get(i)) {
                if (t.getType() == DelimiterType.ARROW) {
                    lambdaIndex = i;
                }
            }
        }

        if (token.getType() != ParenthesisType.RIGHT) {
            tokens.back();
            do {
                if (token.getType() == DelimiterType.COMMA) {
                    index++;
                }

                if (index == lambdaIndex) {
                    break;
                }

                arguments.add(parse(tokens, context));
                token = tokens.next();
                if ((token.getType() != DelimiterType.COMMA) && (token.getType() != ParenthesisType.RIGHT)) {
                    throw new RuntimeException();
                }

            } while (token.getType() == DelimiterType.COMMA);
        }

        boolean seenArrow = false;
        int size = tokens.size();
        while (tokens.getPosition() < size) {
            token = tokens.next();

            if (token.getType() == DelimiterType.ARROW) {
                seenArrow = true;
                break;
            }
        }

        if (!seenArrow) {
            throw new UnexpectedTokenException(tokens, token);
        }

        Tokens remaining = tokens.slice(tokens.getPosition(), tokens.size() - 1);
        return lambdaEvaluator.evaluate(arguments, lambdaArguments, remaining, context);
    }

    protected T parseFunction(Tokens tokens, Context<T> context) throws SyntaxException {
        String name = tokens.next().getData();
        tokens.next();

        List<T> arguments = new ArrayList<>();
        Token token;
        try {
            token = tokens.next();
        } catch (IndexOutOfBoundsException e) {
            int offset = tokens.getPosition() < tokens.size()
                ? tokens.getPosition()
                : tokens.size() - 1;

            throw new UnexpectedTokenException(tokens, offset);
        }

        if (token.getType() != ParenthesisType.RIGHT) {
            tokens.back();
            do {
                arguments.add(parse(tokens, context));
                token = tokens.next();
                if ((token.getType() != DelimiterType.COMMA) && (token.getType() != ParenthesisType.RIGHT)) {
                    throw new RuntimeException();
                }
            } while (token.getType() == DelimiterType.COMMA);
        }

        Function<T> function = cast(getFunctionClass(), tokenizer.getOptions().findFunction(name).orElseThrow());
        return function.apply(arguments);
    }
}
