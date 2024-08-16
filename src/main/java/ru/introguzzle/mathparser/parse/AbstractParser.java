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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractParser<T extends Number> implements Parser<T> {
    static final Operator<?> SPECIAL_UNARY_MINUS = new SubtractionOperator();
    static final Operator<?> SPECIAL_UNARY_PLUS = new AdditionOperator();

    protected final Tokenizer tokenizer;

    protected abstract @NotNull Class<? extends LambdaEvaluator<T>> getLambdaClass();
    protected abstract @NotNull Class<? extends Operator<T>> getOperatorClass();
    protected abstract @NotNull Class<? extends Function<T>> getFunctionClass();
    protected abstract @NotNull Class<? extends ImmutableSymbol<T>> getSymbolClass();

    private static <U extends Nameable, N extends Nameable> U cast(Class<U> cls, N o) {
        if (!cls.isInstance(o)) {
            String format = "Cannot cast %s to class %s";
            String message = String.format(format, o.describe(), cls.getSimpleName());
            throw new UnsupportedOperationException(message);
        }

        return cls.cast(o);
    }

    // Standard operations such as negation and addition

    /**
     * @param left Left operand
     * @param right Right operand
     * @return
     * {@code true} - if left > right <br>
     * {@code false} - if left <= right
     */
    public abstract boolean compare(T left, T right);
    public abstract T absentValue() throws SyntaxException;
    public abstract T negateValue(T value) throws SyntaxException;
    public abstract T add(T left, T right) throws SyntaxException;
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
        Token token = tokens.getNextToken();

        if (token.getType().isTerminal()) {
            return absentValue();
        }

        tokens.returnBack();
        return parseExpression(tokens, context, Integer.MAX_VALUE);
    }

    protected T parseExpression(Tokens tokens, Context<T> context, int priority) throws SyntaxException {
        T leftValue = parseFactor(tokens, context);

        while (true) {
            Token token = tokens.getNextToken();
            if (token.getType() == UnitType.UNIT) {
                return parseUnit(leftValue, tokens, context);
            }

            Optional<Operator<?>> optional = tokenizer.getOptions().findOperator(token.getData());

            if (optional.isEmpty() || !(getOperatorClass().isInstance(optional.get())) || optional.get().getPriority() >= priority) {
                tokens.returnBack();
                break;
            }

            Operator<T> operator = getOperatorClass().cast(optional.get());

            int nextPriority = operator.isRightAssociative()
                    ? operator.getPriority() + 1
                    : operator.getPriority();

            T rightValue = parseExpression(tokens, context, nextPriority);
            leftValue = operator.apply(List.of(leftValue, rightValue));
        }

        return leftValue;
    }

    protected T parseFactor(Tokens tokens, Context<T> context) throws SyntaxException {
        Token token = tokens.getNextToken();

        Type type;
        switch (type = token.getType()) {
            case FunctionType.FUNCTION:
                tokens.returnBack();
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
                token = tokens.getNextToken();
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
        String name = tokens.getNextToken().getData();

        LambdaEvaluator<T> lambdaEvaluator = cast(getLambdaClass(), getTokenizer().getOptions().findLambdaEvaluator(name).orElseThrow());

        tokens.getNextToken();

        List<T> arguments = new ArrayList<>();
        List<LambdaArgument<T>> lambdaArguments = new ArrayList<>();
        Token token;

        try {
            token = tokens.getNextToken();
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

        int groupIndex = 0;

        if (token.getType() != ParenthesisType.RIGHT) {
            tokens.returnBack();
            do {
                if (token.getType() == DelimiterType.COMMA) {
                    groupIndex++;
                }

                if (groupIndex == lambdaEvaluator.getLambdaGroupIndex()) {
                    break;
                }

                arguments.add(parse(tokens, context));
                token = tokens.getNextToken();
                if ((token.getType() != DelimiterType.COMMA) && (token.getType() != ParenthesisType.RIGHT)) {
                    throw new RuntimeException();
                }

            } while (token.getType() == DelimiterType.COMMA);
        }

        boolean seenArrow = false;
        int size = tokens.size();
        while (tokens.getPosition() < size) {
            token = tokens.getNextToken();

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
        String name = tokens.getNextToken().getData();
        tokens.getNextToken();

        List<T> arguments = new ArrayList<>();
        Token token;
        try {
            token = tokens.getNextToken();
        } catch (IndexOutOfBoundsException e) {
            int offset = tokens.getPosition() < tokens.size()
                ? tokens.getPosition()
                : tokens.size() - 1;

            throw new UnexpectedTokenException(tokens, offset);
        }

        if (token.getType() != ParenthesisType.RIGHT) {
            tokens.returnBack();
            do {
                arguments.add(parse(tokens, context));
                token = tokens.getNextToken();
                if ((token.getType() != DelimiterType.COMMA) && (token.getType() != ParenthesisType.RIGHT)) {
                    throw new RuntimeException();
                }
            } while (token.getType() == DelimiterType.COMMA);
        }

        Function<T> function = cast(getFunctionClass(), tokenizer.getOptions().findFunction(name).orElseThrow());
        return function.apply(arguments);
    }
}
