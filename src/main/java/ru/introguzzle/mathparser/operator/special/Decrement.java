package ru.introguzzle.mathparser.operator.special;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.operator.DoubleUnaryOperator;
import ru.introguzzle.mathparser.operator.Priorities;

import java.util.List;

public class Decrement implements DoubleUnaryOperator {
    @Override
    public Double apply(List<Double> doubles) {
        return doubles.getFirst() - 1;
    }

    @Override
    public @NotNull String getName() {
        return "--";
    }

    @Override
    public Association getAssociation() {
        return Association.LEFT;
    }

    @Override
    public int getPriority() {
        return Priorities.UNARY_PRIORITY;
    }
}
