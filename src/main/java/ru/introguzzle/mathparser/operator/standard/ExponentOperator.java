package ru.introguzzle.mathparser.operator.standard;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.operator.BinaryOperator;
import ru.introguzzle.mathparser.operator.Priorities;

import java.util.List;

public class ExponentOperator implements BinaryOperator {
    @Override
    public Double apply(List<Double> operands) {
        return Math.pow(operands.get(0), operands.get(1));
    }

    @Override
    public int getPriority() {
        return Priorities.EXPONENT_PRIORITY;
    }

    @Override
    public Association getAssociation() {
        return Association.RIGHT;
    }

    @Override
    public @NotNull String getName() {
        return "**";
    }
}
