package ru.introguzzle.mathparser.parse;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.common.math.algebra.Algebra;
import ru.introguzzle.mathparser.common.math.algebra.BigDecimalAlgebra;
import ru.introguzzle.mathparser.constant.bigdecimal.BigDecimalConstant;
import ru.introguzzle.mathparser.constant.bigdecimal.BigDecimalConstantReflector;
import ru.introguzzle.mathparser.function.Function;
import ru.introguzzle.mathparser.function.bigdecimal.BigDecimalFunction;
import ru.introguzzle.mathparser.function.bigdecimal.BigDecimalFunctionReflector;
import ru.introguzzle.mathparser.lambda.LambdaEvaluator;
import ru.introguzzle.mathparser.lambda.bigdecimal.BigDecimalLambdaEvaluator;
import ru.introguzzle.mathparser.operator.Operator;
import ru.introguzzle.mathparser.operator.bigdecimal.BigDecimalOperator;
import ru.introguzzle.mathparser.operator.bigdecimal.BigDecimalOperatorReflector;
import ru.introguzzle.mathparser.symbol.ImmutableSymbol;
import ru.introguzzle.mathparser.tokenize.MathTokenizer;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;

public class BigDecimalParser extends AbstractParser<BigDecimal> {
    public static final MathContext M = MathContext.DECIMAL128;
    private final Algebra<BigDecimal> algebra = new BigDecimalAlgebra();

    public BigDecimalParser() {
        super(new MathTokenizer(
                BigDecimalFunctionReflector.get(),
                BigDecimalConstantReflector.get(),
                BigDecimalOperatorReflector.get(),
                Map.of(),
                Map.of()
        ));
    }

    @Override
    protected @NotNull Class<? extends LambdaEvaluator<BigDecimal>> getLambdaClass() {
        return BigDecimalLambdaEvaluator.class;
    }

    @Override
    protected @NotNull Class<? extends Operator<BigDecimal>> getOperatorClass() {
        return BigDecimalOperator.class;
    }

    @Override
    protected @NotNull Class<? extends Function<BigDecimal>> getFunctionClass() {
        return BigDecimalFunction.class;
    }

    @Override
    protected @NotNull Class<? extends ImmutableSymbol<BigDecimal>> getSymbolClass() {
        return BigDecimalConstant.class;
    }

    @Override
    public Algebra<BigDecimal> getAlgebra() {
        return algebra;
    }

    @Override
    public NumberConverter<BigDecimal> getConverter() {
        return string -> new BigDecimal(string, M);
    }
}
