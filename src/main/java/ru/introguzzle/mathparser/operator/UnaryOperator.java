package ru.introguzzle.mathparser.operator;

public interface UnaryOperator<T extends Number> extends Operator<T> {
    @Override
    default int operands() {
        return 1;
    }
}
