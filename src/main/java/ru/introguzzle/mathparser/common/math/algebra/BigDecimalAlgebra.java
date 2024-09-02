package ru.introguzzle.mathparser.common.math.algebra;

import java.math.BigDecimal;
import java.math.MathContext;

public class BigDecimalAlgebra implements Algebra<BigDecimal> {
    @Override
    public int compare(BigDecimal left, BigDecimal right) throws UnsupportedAlgebraOperationException {
        return left.compareTo(right);
    }

    @Override
    public BigDecimal absentValue() throws UnsupportedAlgebraOperationException {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal negateValue(BigDecimal value) throws UnsupportedAlgebraOperationException {
        return value.negate(MathContext.DECIMAL128);
    }

    @Override
    public BigDecimal add(BigDecimal left, BigDecimal right) throws UnsupportedAlgebraOperationException {
        return left.add(right, MathContext.DECIMAL128);
    }
}
