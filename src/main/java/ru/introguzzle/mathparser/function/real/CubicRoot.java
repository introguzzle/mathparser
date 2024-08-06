package ru.introguzzle.mathparser.function.real;

import java.util.List;

public class CubicRoot extends DoubleFunction {
    public CubicRoot() {
        super("cbrt", 1);
    }

    @Override
    public Double apply(List<Double> arguments) {
        return Math.cbrt(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
