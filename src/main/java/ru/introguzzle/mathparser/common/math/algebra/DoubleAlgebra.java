package ru.introguzzle.mathparser.common.math.algebra;

public class DoubleAlgebra implements Algebra<Double> {
    @Override
    public int compare(Double left, Double right) throws UnsupportedAlgebraOperationException {
        return left.compareTo(right);
    }

    @Override
    public Double absentValue() throws UnsupportedAlgebraOperationException {
        return 0.0;
    }

    @Override
    public Double negateValue(Double value) throws UnsupportedAlgebraOperationException {
        return -value;
    }

    @Override
    public Double add(Double left, Double right) throws UnsupportedAlgebraOperationException {
        return left + right;
    }
}
