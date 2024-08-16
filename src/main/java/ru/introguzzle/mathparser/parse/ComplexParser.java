package ru.introguzzle.mathparser.parse;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.common.naming.Context;
import ru.introguzzle.mathparser.complex.Complex;
import ru.introguzzle.mathparser.constant.complex.ComplexConstant;
import ru.introguzzle.mathparser.constant.complex.ComplexConstantReflector;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.function.complex.ComplexFunction;
import ru.introguzzle.mathparser.function.complex.ComplexFunctionReflector;
import ru.introguzzle.mathparser.lambda.LambdaEvaluator;
import ru.introguzzle.mathparser.lambda.complex.ComplexLambdaEvaluator;
import ru.introguzzle.mathparser.operator.Operator;
import ru.introguzzle.mathparser.operator.complex.ComplexOperator;
import ru.introguzzle.mathparser.operator.complex.ComplexOperatorReflector;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;
import ru.introguzzle.mathparser.tokenize.MathTokenizer;
import ru.introguzzle.mathparser.tokenize.Tokenizer;
import ru.introguzzle.mathparser.tokenize.token.NumberToken;
import ru.introguzzle.mathparser.tokenize.token.Token;
import ru.introguzzle.mathparser.tokenize.token.Tokens;
import ru.introguzzle.mathparser.tokenize.token.type.NumberType;
import ru.introguzzle.mathparser.tokenize.token.type.SymbolType;

import java.util.Map;
import java.util.Optional;

public class ComplexParser extends AbstractParser<Complex> {
    public ComplexParser() {
        super(new MathTokenizer(
                ComplexFunctionReflector.get(),
                ComplexConstantReflector.get(),
                ComplexOperatorReflector.get(),
                Map.of(),
                Map.of()
        ));
    }

    public ComplexParser(Tokenizer tokenizer) {
        super(tokenizer);
    }

    @Override
    public @NotNull Class<? extends LambdaEvaluator<Complex>> getLambdaClass() {
        return ComplexLambdaEvaluator.class;
    }

    @Override
    public @NotNull Class<? extends Operator<Complex>> getOperatorClass() {
        return ComplexOperator.class;
    }

    @Override
    public @NotNull Class<? extends Function<Complex>> getFunctionClass() {
        return ComplexFunction.class;
    }

    @Override
    public @NotNull Class<? extends ImmutableSymbol<Complex>> getSymbolClass() {
        return ComplexConstant.class;
    }

    @Override
    public Complex absentValue() {
        return Complex.ZERO;
    }

    @Override
    public Complex negateValue(Complex value) {
        return value.negate();
    }

    @Override
    public Complex add(Complex left, Complex right) throws SyntaxException {
        return left.add(right);
    }

    @Override
    public Complex parseUnit(Complex value, Tokens tokens, Context<Complex> context) {
        throw new UnsupportedOperationException("Can't convert complex numbers");
    }

    @Override
    public NumberConverter<Complex> getConverter() {
        return NumberConverter.getComplexConverter();
    }

    @Override
    public boolean compare(Complex left, Complex right) {
        return left.doubleValue() > right.doubleValue();
    }

    @Override
    protected Complex parseFactor(Tokens tokens, Context<Complex> context) throws SyntaxException {
        Token token = tokens.getNextToken();

        switch (token.getType()) {
            case NumberType.NUMBER:
            case NumberType.COMPLEX_NUMBER:
                if (token instanceof NumberToken numberToken) {
                    String plain = numberToken.getNumber().getPlain();
                    return getConverter().convert(plain);
                }

            case SymbolType.CONSTANT:
            case SymbolType.COMPLEX_CONSTANT:
                Optional<ImmutableSymbol<?>> symbol = tokenizer.getOptions().findConstant(token.getData());
                if (symbol.isPresent() && getSymbolClass().isInstance(symbol.get())) {
                    return getSymbolClass().cast(symbol.get()).getValue();
                }

                throw new UnexpectedTokenException(tokens, token);

            default:
                tokens.returnBack();
                return super.parseFactor(tokens, context);
        }
    }
}
