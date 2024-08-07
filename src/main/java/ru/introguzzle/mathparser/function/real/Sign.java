package ru.introguzzle.mathparser.function.real;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Sign extends DoubleFunction {
    public Sign() {
        super("sgn", 1);
    }

    @Override
    public @NotNull Double apply(List<Double> arguments) {
        return Math.signum(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
