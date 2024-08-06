package ru.introguzzle.mathparser.operator.logical;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.operator.DoubleBinaryOperator;
import ru.introguzzle.mathparser.operator.Priorities;

import java.util.List;

public class ConjuctionOperator implements DoubleBinaryOperator {
    @Override
    public Double apply(List<Double> operands) {
        return (double) (operands.get(0).longValue() & operands.get(1).longValue());
    }

    @Override
    public int getPriority() {
        return Priorities.AND_PRIORITY;
    }

    @Override
    public Association getAssociation() {
        return Association.LEFT;
    }

    @Override
    public @NotNull String getName() {
        return "&";
    }
}
