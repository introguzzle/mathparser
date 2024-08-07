package ru.introguzzle.mathparser.parse;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.Context;
import ru.introguzzle.mathparser.common.NamingContext;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.complex.Complex;
import ru.introguzzle.mathparser.constant.complex.ComplexConstant;
import ru.introguzzle.mathparser.constant.complex.ComplexConstantReflector;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.expression.MathExpression;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.function.complex.ComplexFunction;
import ru.introguzzle.mathparser.function.complex.ComplexFunctionReflector;
import ru.introguzzle.mathparser.operator.Operator;
import ru.introguzzle.mathparser.operator.complex.ComplexOperator;
import ru.introguzzle.mathparser.operator.complex.ComplexOperatorReflector;
import ru.introguzzle.mathparser.operator.complex.ComplexUnaryOperator;
import ru.introguzzle.mathparser.operator.standard.AdditionOperator;
import ru.introguzzle.mathparser.operator.standard.SubtractionOperator;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;
import ru.introguzzle.mathparser.tokenize.MathTokenizer;
import ru.introguzzle.mathparser.tokenize.TokenizeException;
import ru.introguzzle.mathparser.tokenize.Tokenizer;
import ru.introguzzle.mathparser.tokenize.token.NumberToken;
import ru.introguzzle.mathparser.tokenize.token.Token;
import ru.introguzzle.mathparser.tokenize.token.Tokens;
import ru.introguzzle.mathparser.tokenize.token.type.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComplexParser implements Parser<Complex>, Serializable {
    protected final Tokenizer tokenizer;
    private static final Operator<Double> SPECIAL_UNARY_MINUS = new SubtractionOperator();
    private static final Operator<Double> SPECIAL_UNARY_PLUS = new AdditionOperator();

    public ComplexParser() {
        this.tokenizer = new MathTokenizer(
                ComplexFunctionReflector.get(),
                ComplexConstantReflector.get(),
                ComplexOperatorReflector.get()
        );
    }

    public static void main(String[] args) throws SyntaxException {
        ComplexParser p = new ComplexParser();
        Expression e = new MathExpression("1 / 4");
        System.out.println(p.parse(e));
    }

    public ComplexParser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Serial
    private static final long serialVersionUID = -5531541301527215714L;

    protected Tokens tokenize(Expression expression, Context<Complex> context) throws TokenizeException {
        return tokenizer.tokenize(expression, context).getTokens();
    }

    @Override
    public Complex parse(@NotNull Expression expression) throws SyntaxException {
        return parse(expression, new NamingContext<>());
    }

    @Override
    public Complex parse(@NotNull Expression expression, @NotNull Context<Complex> context) throws SyntaxException {
        Tokens tokens = tokenize(expression, context);
        return parse(tokens, context);
    }

    @Override
    public Complex parse(@NotNull Tokens tokens, Context<Complex> context) throws SyntaxException {
        Token token = tokens.getNextToken();

        if (token.getType().isTerminal()) {
            return Complex.NAN;
        }

        tokens.returnBack();
        return parseExpression(tokens, context, Integer.MAX_VALUE);
    }

    @Override
    public Tokenizer getTokenizer() {
        return tokenizer;
    }

    private Complex parseExpression(Tokens tokens, Context<Complex> context, int priority) throws SyntaxException {
        Complex leftValue = parseFactor(tokens, context);

        while (true) {
            Token token = tokens.getNextToken();
            Optional<Operator<?>> optional = tokenizer.getOptions().findOperator(token.getData());

            if (optional.isEmpty() || !(optional.get() instanceof ComplexOperator operator) || optional.get().getPriority() >= priority) {
                tokens.returnBack();
                break;
            }

            int nextPriority = operator.isRightAssociative()
                    ? operator.getPriority() + 1
                    : operator.getPriority();

            Complex rightValue = parseExpression(tokens, context, nextPriority);
            leftValue = operator.apply(List.of(leftValue, rightValue));
        }

        return leftValue;
    }

    private Complex parseFactor(Tokens tokens, Context<Complex> context) throws SyntaxException {
        Token token = tokens.getNextToken();

        switch (token.getType()) {
            case FunctionType.FUNCTION:
                tokens.returnBack();
                return parseFunction(tokens, context);

            case OperatorType.OPERATOR:
                if (token.getData().equals(SPECIAL_UNARY_MINUS.getName())) {
                    Complex complex = parseFactor(tokens, context);
                    return complex.negate();
                }

                if (token.getData().equals(SPECIAL_UNARY_PLUS.getName())) {
                    return parseFactor(tokens, context);
                }

            case NumberType.COMPLEX_NUMBER:
                if (token instanceof NumberToken numberToken) {
                    String plain = numberToken.getNumber().getPlain();
                    return Complex.parseComplex(plain);
                }

            case NumberType.NUMBER:
                if (token instanceof NumberToken numberToken) {
                    String plain = numberToken.getNumber().getPlain();
                    return Complex.of(Double.parseDouble(plain));
                }

                return Complex.of(Double.parseDouble(token.getData()));

            case SymbolType.CONSTANT:
                Optional<ImmutableSymbol<?>> symbol = tokenizer.getOptions().findConstant(token);
                if (symbol.isPresent() && symbol.get() instanceof ComplexConstant constant) {
                    return constant.getValue();
                }

                throw new UnexpectedTokenException(tokens, token);

            case SymbolType.VARIABLE:
            case SymbolType.COEFFICIENT:
                return context.getSymbol(token.getData()).orElseThrow().getValue();

            case ParenthesisType.LEFT:
                Complex value = parse(tokens, context);
                token = tokens.getNextToken();
                if (token.getType() != ParenthesisType.RIGHT) {
                    throw new UnexpectedTokenException(tokens, token);
                }

                return value;

            default:
                if (token.getType() instanceof OperatorType) {
                    Operator<?> operator = tokenizer.getOptions().findOperator(token.getData()).orElseThrow();
                    if (operator instanceof ComplexUnaryOperator o) {
                        return o.apply(List.of(parseFactor(tokens, context)));
                    }
                }

                throw new UnexpectedTokenException(tokens, token);
        }
    }

    private Complex parseFunction(Tokens tokens, Context<Complex> context) throws SyntaxException {
        String name = tokens.getNextToken().getData();
        tokens.getNextToken();

        List<Complex> arguments = new ArrayList<>();
        Token token = tokens.getNextToken();

        if (token.getType() != ParenthesisType.RIGHT) {
            tokens.returnBack();
            do {
                arguments.add(parse(tokens, context));
                token = tokens.getNextToken();
                if ((token.getType() != SpecialType.COMMA) && (token.getType() != ParenthesisType.RIGHT)) {
                    throw new RuntimeException();
                }
            } while (token.getType() == SpecialType.COMMA);
        }

        Optional<Function<?>> optional = tokenizer.getOptions().findFunction(name);
        int given = arguments.size();

        if (optional.isEmpty() || !(optional.get() instanceof ComplexFunction function)) {
            throw new UnsupportedOperationException("Can't cast function to ComplexFunction instance");
        }

        if (function.isVariadic()) {
            if (function.getRequiredArguments() > given) {
                throw function.createException(given);
            } else {
                return function.apply(arguments);
            }
        }

        if (function.getRequiredArguments() == given) {
            return function.apply(arguments);
        }

        throw function.createException(given);
    }
}
