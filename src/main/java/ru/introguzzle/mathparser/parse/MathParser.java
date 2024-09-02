package ru.introguzzle.mathparser.parse;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.math.algebra.Algebra;
import ru.introguzzle.mathparser.common.math.algebra.DoubleAlgebra;
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
import ru.introguzzle.mathparser.tokenize.token.Tokens;
import ru.introguzzle.mathparser.unit.Unit;

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
    private final Algebra<Double> algebra = new DoubleAlgebra();

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
    public NumberConverter<Double> getConverter() {
        return NumberConverter.getDoubleConverter();
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

    @Override
    public Algebra<Double> getAlgebra() {
        return algebra;
    }
}
