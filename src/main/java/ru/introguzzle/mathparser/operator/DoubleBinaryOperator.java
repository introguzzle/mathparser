package ru.introguzzle.mathparser.operator;

public interface DoubleBinaryOperator extends DoubleOperator {
    @Override
    default int getRequiredOperands() {
        return BINARY;
    }
}
