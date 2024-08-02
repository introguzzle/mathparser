package ru.introguzzle.mathparser.operator;

public interface BinaryOperator extends Operator {
    @Override
    default int getRequiredOperands() {
        return BINARY;
    }
}
