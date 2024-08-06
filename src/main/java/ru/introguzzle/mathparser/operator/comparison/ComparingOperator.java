package ru.introguzzle.mathparser.operator.comparison;

import ru.introguzzle.mathparser.operator.DoubleBinaryOperator;
import ru.introguzzle.mathparser.operator.Priorities;

import java.util.List;

public abstract class ComparingOperator implements DoubleBinaryOperator {
    @Override
    public int getPriority() {
        return Priorities.COMPARISON_PRIORITY;
    }

    @Override
    public Association getAssociation() {
        return Association.LEFT;
    }

    @Override
    public Double apply(List<Double> doubles) {
        return compare(doubles.getFirst(), doubles.getLast()) ? 1.0 : 0.0;
    }

    public abstract boolean compare(double left, double right);
}
