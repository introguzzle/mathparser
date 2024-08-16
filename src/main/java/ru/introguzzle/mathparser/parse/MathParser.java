package ru.introguzzle.mathparser.parse;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.common.naming.Context;
import ru.introguzzle.mathparser.constant.real.DoubleConstant;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.function.real.DoubleFunction;
import ru.introguzzle.mathparser.lambda.LambdaEvaluator;
import ru.introguzzle.mathparser.lambda.real.DoubleLambdaEvaluator;
import ru.introguzzle.mathparser.lambda.real.SumDeltaDoubleLambdaEvaluator;
import ru.introguzzle.mathparser.lambda.real.SumDoubleLambdaEvaluator;
import ru.introguzzle.mathparser.operator.DoubleOperator;
import ru.introguzzle.mathparser.operator.Operator;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;
import ru.introguzzle.mathparser.tokenize.MathTokenizer;
import ru.introguzzle.mathparser.tokenize.Tokenizer;
import ru.introguzzle.mathparser.tokenize.token.Token;
import ru.introguzzle.mathparser.tokenize.token.Tokens;
import ru.introguzzle.mathparser.tokenize.token.type.OperatorType;
import ru.introguzzle.mathparser.unit.Unit;
import ru.introguzzle.mathparser.unit.measure.MeasureException;

public class MathParser extends AbstractParser<Double> {

    public MathParser() {
        super(new MathTokenizer());
        tokenizer.getOptions().addLambdaEvaluator(new SumDoubleLambdaEvaluator(this))
                .addLambdaEvaluator(new SumDeltaDoubleLambdaEvaluator(this));
    }

    public MathParser(Tokenizer tokenizer) {
        super(tokenizer);
    }

    @Override
    protected @NotNull Class<? extends LambdaEvaluator<Double>> getLambdaClass() {
        return DoubleLambdaEvaluator.class;
    }

    @Override
    protected @NotNull Class<? extends Operator<Double>> getOperatorClass() {
        return DoubleOperator.class;
    }

    @Override
    protected @NotNull Class<? extends Function<Double>> getFunctionClass() {
        return DoubleFunction.class;
    }

    @Override
    protected @NotNull Class<? extends ImmutableSymbol<Double>> getSymbolClass() {
        return DoubleConstant.class;
    }

    @Override
    public Double absentValue() {
        return 0.0;
    }

    @Override
    public Double negateValue(Double value) {
        return -value;
    }

    @Override
    public Double add(Double left, Double right) throws SyntaxException {
        return left + right;
    }

    @Override
    public Double parseUnit(Double value, Tokens tokens, Context<Double> context) throws SyntaxException {
        tokens.returnBack();
        Token token = tokens.getNextToken();

        Unit<?, ?> from = getTokenizer().getOptions()
                .findUnit(token.getData())
                .orElseThrow();

        Token nextToken = tokens.getNextToken();
        if (nextToken.getType() != OperatorType.CONVERTER) {
            throw new UnexpectedTokenException(tokens, nextToken);
        }

        Unit<?, ?> to = getTokenizer().getOptions()
                .findUnit(tokens.getNextToken().getData())
                .orElseThrow();

        if (from.getMeasure() != to.getMeasure()) {
            throw new MeasureException(from.getMeasure(), to.getMeasure());
        }

        try {
            // Measures should be same type, but actual runtime classes are totally unsafe
            @SuppressWarnings("unchecked")
            double convertedValue = ((Unit) from).transform(value, to);
            return convertedValue;
        } catch (ClassCastException e) {
            String format = """
                    \s
                    Failed to convert value %f from unit '%s' to unit '%s'.\s
                    Incompatible types: cannot cast unit of type '%s' to unit of type '%s'.
                    %s with name %s must inherit from the same parent with %s.\s""";


            String target = to.getClass().isAnonymousClass()
                    ? "Anonymous class"
                    : to.describe();

            String message = String.format(format, value, from.getName(), to.getName(),
                    from.describe(), target,
                    target, to.getName(), from.describe()
            );

            throw new UnsupportedOperationException(message, e);
        }
    }

    @Override
    public NumberConverter<Double> getConverter() {
        return NumberConverter.getDoubleConverter();
    }

    @Override
    public boolean compare(Double left, Double right) {
        return left > right;
    }
}
