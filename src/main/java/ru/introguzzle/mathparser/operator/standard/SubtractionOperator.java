package ru.introguzzle.mathparser.operator.standard;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.operator.DoubleBinaryOperator;
import ru.introguzzle.mathparser.operator.Priorities;

import java.util.List;

public class SubtractionOperator implements DoubleBinaryOperator {
    @Override
    public Double apply(List<Double> operands) {
        return operands.get(0) - operands.get(1);
    }

    @Override
    public int getPriority() {
        return Priorities.ADDITION_PRIORITY;
    }

    @Override
    public Association getAssociation() {
        return Association.LEFT;
    }

    @Override
    public @NotNull String getName() {
        return "-";
    }
}
