package ru.introguzzle.mathparser.operator;

public interface BinaryOperator<T extends Number> extends Operator<T> {
    @Override
    default int operands() {
        return 2;
    }
}