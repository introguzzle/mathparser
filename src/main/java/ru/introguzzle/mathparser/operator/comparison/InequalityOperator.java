package ru.introguzzle.mathparser.operator.comparison;

import org.jetbrains.annotations.NotNull;

public class InequalityOperator extends ComparingOperator {
    @Override
    public boolean compare(double left, double right) {
        return Double.compare(left, right) != 0;
    }

    @Override
    public @NotNull String getName() {
        return "!=";
    }
}
