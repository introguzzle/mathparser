package ru.introguzzle.mathparser.operator.bigdecimal;

import ru.introguzzle.mathparser.operator.Operator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public interface BigDecimalOperator extends Operator<BigDecimal> {
    int UNARY = 1;
    int BINARY = 2;
    MathContext MATH_CONTEXT = MathContext.DECIMAL128;

    @Override
    BigDecimal apply(List<BigDecimal> operands);
}
