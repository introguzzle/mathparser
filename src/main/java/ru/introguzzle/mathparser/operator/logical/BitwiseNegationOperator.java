package ru.introguzzle.mathparser.operator.logical;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.operator.Priorities;
import ru.introguzzle.mathparser.operator.UnaryOperator;

import java.util.List;

public class BitwiseNegationOperator implements UnaryOperator {
    @Override
    public Double apply(List<Double> operands) {
        return (double) (~operands.getFirst().longValue());
    }

    @Override
    public int getPriority() {
        return Priorities.XOR_PRIORITY;
    }

    @Override
    public Association getAssociation() {
        return Association.LEFT;
    }

    @Override
    public @NotNull String getName() {
        return "~";
    }
}
