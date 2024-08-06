package ru.introguzzle.mathparser.operator.complex;

import ru.introguzzle.mathparser.complex.Complex;
import ru.introguzzle.mathparser.operator.Operator;

public interface ComplexUnaryOperator extends Operator<Complex> {
    @Override
    default int getRequiredOperands() {
        return UNARY;
    }
}
