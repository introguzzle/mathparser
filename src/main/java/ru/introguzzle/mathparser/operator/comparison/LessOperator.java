package ru.introguzzle.mathparser.operator.comparison;

import org.jetbrains.annotations.NotNull;

public class LessOperator extends ComparingOperator {
    @Override
    public @NotNull String getName() {
        return "<";
    }

    @Override
    public boolean compare(double left, double right) {
        return left < right;
    }
}
