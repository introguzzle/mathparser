package ru.introguzzle.mathparser.operator.comparison;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class EqualityOperator extends ComparingOperator {
    @Override
    public Double apply(List<Double> operands) {
        return Objects.equals(operands.get(0), operands.get(1)) ? 1.0 : 0.0;
    }

    @Override
    public @NotNull String getName() {
        return "==";
    }
}
