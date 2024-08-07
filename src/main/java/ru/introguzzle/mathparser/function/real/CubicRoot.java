package ru.introguzzle.mathparser.function.real;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CubicRoot extends DoubleFunction {
    public CubicRoot() {
        super("cbrt", 1);
    }

    @Override
    public @NotNull Double apply(List<Double> arguments) {
        return Math.cbrt(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
