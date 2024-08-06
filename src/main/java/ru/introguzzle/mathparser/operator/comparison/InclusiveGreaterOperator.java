package ru.introguzzle.mathparser.operator.comparison;

import org.jetbrains.annotations.NotNull;

public class InclusiveGreaterOperator extends ComparingOperator {
    @Override
    public boolean compare(double left, double right) {
        return left >= right;
    }

    @Override
    public @NotNull String getName() {
        return ">=";
    }
}
