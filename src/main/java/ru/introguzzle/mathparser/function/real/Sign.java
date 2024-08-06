package ru.introguzzle.mathparser.function.real;

import java.util.List;

public class Sign extends DoubleFunction {
    public Sign() {
        super("sgn", 1);
    }

    @Override
    public Double apply(List<Double> arguments) {
        return Math.signum(arguments.getFirst());
    }

    @Override
    public boolean isVariadic() {
        return false;
    }
}
