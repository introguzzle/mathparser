package ru.introguzzle.mathparser.operator.shift;

import org.jetbrains.annotations.NotNull;
import ru.introguzzle.mathparser.operator.BinaryOperator;
import ru.introguzzle.mathparser.operator.Priorities;

import java.util.List;

public class LeftBitwiseShiftOperator implements BinaryOperator {
    @Override
    public Double apply(List<Double> operands) {
        return (double) (operands.get(0).longValue() << operands.get(1).longValue());
    }

    @Override
    public int getPriority() {
        return Priorities.SHIFT_PRIORITY;
    }

    @Override
    public Association getAssociation() {
        return Association.LEFT;
    }

    @Override
    public @NotNull String getName() {
        return "<<";
    }
}
