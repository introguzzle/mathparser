package ru.introguzzle.mathparser.common.math.algebra;

import ru.introguzzle.mathparser.complex.Complex;

public class ComplexAlgebra implements Algebra<Complex> {
    @Override
    public int compare(Complex left, Complex right) throws UnsupportedAlgebraOperationException {
        throw new UnsupportedAlgebraOperationException("Can't compare complex numbers");
    }

    @Override
    public Complex absentValue() throws UnsupportedAlgebraOperationException {
        return Complex.ZERO;
    }

    @Override
    public Complex negateValue(Complex value) throws UnsupportedAlgebraOperationException {
        return value.negate();
    }

    @Override
    public Complex add(Complex left, Complex right) throws UnsupportedAlgebraOperationException {
        return left.add(right);
    }
}
