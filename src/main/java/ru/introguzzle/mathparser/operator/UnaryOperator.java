package ru.introguzzle.mathparser.operator;

public interface UnaryOperator extends Operator {
    @Override
    default int getRequiredOperands() {
        return UNARY;
    }
}
