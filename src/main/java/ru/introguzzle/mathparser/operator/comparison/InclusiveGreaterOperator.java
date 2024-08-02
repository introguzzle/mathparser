package ru.introguzzle.mathparser.operator.comparison;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InclusiveGreaterOperator extends ComparingOperator {
    @Override
    public Double apply(List<Double> operands) {
        return operands.get(0) >= operands.get(1) ? 1.0 : 0.0;
    }

    @Override
    public @NotNull String getName() {
        return ">=";
    }
}
