package ru.introguzzle.mathparser.operator.complex;

public interface ComplexBinaryOperator extends ComplexOperator {
    @Override
    default int getRequiredOperands() {
        return BINARY;
    }
}
