package ru.introguzzle.mathparser.operator;

public interface DoubleUnaryOperator extends DoubleOperator {
    @Override
    default int getRequiredOperands() {
        return UNARY;
    }
}
