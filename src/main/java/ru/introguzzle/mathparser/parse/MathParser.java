package ru.introguzzle.mathparser.parse;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.SyntaxException;
import ru.introguzzle.mathparser.common.naming.Context;
import ru.introguzzle.mathparser.constant.real.DoubleConstant;
import ru.introguzzle.mathparser.expression.Expression;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.function.real.DoubleFunction;
import ru.introguzzle.mathparser.lambda.LambdaEvaluator;
import ru.introguzzle.mathparser.lambda.real.*;
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

/**
 * A parser class for mathematical expressions involving only real values.
 * This class extends AbstractParser and implements specific parsing operations
 * for Double values, including support for units and custom operators, functions,
 * constants, and lambda evaluators.
 *
 * @see AbstractParser
 * @see ComplexParser
 */
public class MathParser extends AbstractParser<Double> {

    /**
     * Default constructor that initializes the parser with default tokenizer and adds
     * built-in lambda evaluators.
     *
     * @see LambdaEvaluator
     * @see Tokenizer
     */
    public MathParser() {
        super(new MathTokenizer());
        getTokenizer().getOptions().addLambdaEvaluator(new SumEvaluator(this))
                .addLambdaEvaluator(new SumDeltaEvaluator(this))
                .addLambdaEvaluator(new FunctionAdapterEvaluator(this))
                .addLambdaEvaluator(new NewtonEvaluator(this));
    }

    /**
     * Constructor that allows providing a tokenizer.
     *
     * @param tokenizer {@link Tokenizer} for obtaining {@link Tokens} from {@link Expression}
     */
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
        tokens.back();
        Token token = tokens.next();

        Unit<?, ?> from = getTokenizer().getOptions()
                .findUnit(token.getData())
                .orElseThrow();

        Token nextToken = tokens.next();
        if (nextToken.getType() != OperatorType.CONVERTER) {
            throw new UnexpectedTokenException(tokens, nextToken);
        }

        Unit<?, ?> to = getTokenizer().getOptions()
                .findUnit(tokens.next().getData())
                .orElseThrow();

        if (!from.getMeasure().equals(to.getMeasure())) {
            throw new MeasureException(from.getMeasure(), to.getMeasure());
        }

        try {
            // Measures should be same type, but actual runtime classes are still unsafe
            @SuppressWarnings("unchecked")
            double convertedValue = ((Unit) from).apply(value, to);
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

    // Customizing methods

    public MathParser addOperator(DoubleOperator operator) {
        getTokenizer().getOptions().addOperator(operator);
        return this;
    }

    public MathParser addFunction(DoubleFunction function) {
        getTokenizer().getOptions().addFunction(function);
        return this;
    }

    public MathParser addConstant(DoubleConstant constant) {
        getTokenizer().getOptions().addConstant(constant);
        return this;
    }

    public MathParser addLambdaEvaluator(DoubleLambdaEvaluator evaluator) {
        getTokenizer().getOptions().addLambdaEvaluator(evaluator);
        return this;
    }

    @Override
    public MathParser addUnit(Unit<?, ?> unit) {
        return (MathParser) super.addUnit(unit);
    }
}
